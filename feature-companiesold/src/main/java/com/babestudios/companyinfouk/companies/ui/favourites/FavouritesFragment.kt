package com.babestudios.companyinfouk.companies.ui.favourites

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.ext.viewBinding
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentFavouritesBinding
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesAdapter
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesItem
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesViewHolder
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val PENDING_REMOVAL_TIMEOUT = 5000 // 5sec
const val DELETE_IN_COMPANY_KEY = "DeletedInCompany"
const val DELETE_IN_COMPANY_BUNDLE_KEY = "DeletedInCompany"
const val STARTUP_DELAY = 30L

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites), MviView<State, UserIntent> {

	private lateinit var favouritesAdapter: FavouritesAdapter

	private val viewModel: FavouritesViewModel by viewModels()

	private val binding by viewBinding<FragmentFavouritesBinding>()

	private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	}

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.FavouritesItemClicked ->
				findNavController().navigateSafe(
					FavouritesFragmentDirections.actionToCompany(
						sideEffect.selectedCompanyNumber,
						sideEffect.companyName
					)
				)
		}
	}

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {

		val tvMsvError = binding.msvFavourites.findViewById<TextView>(R.id.tvMsvError)
		val ivEmptyView = binding.msvFavourites.findViewById<ImageView>(R.id.ivEmptyView)

		when (model) {
			is State.Loading -> binding.msvFavourites.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				binding.msvFavourites.viewState = VIEW_STATE_ERROR
				tvMsvError.text = model.t.message
			}
			is State.Show -> {
				val favourites = model.favourites.toList()
				if (favourites.isEmpty()) {
					binding.msvFavourites.viewState = VIEW_STATE_EMPTY
					ivEmptyView.setImageResource(R.drawable.ic_business_empty_favorites)
				} else {
					binding.msvFavourites.viewState = VIEW_STATE_CONTENT
					if (binding.rvFavourites.adapter == null) {
						favouritesAdapter = FavouritesAdapter(favourites, lifecycleScope)
						binding.rvFavourites.adapter = favouritesAdapter
						favouritesAdapter.itemClicks.onEach {
							dispatch(UserIntent.FavouritesItemClicked(it))
						}.launchIn(lifecycleScope)
						favouritesAdapter.cancelClicks.onEach {
							val pendingRemovalRunnable = pendingRunnables[it.searchHistoryItem]
							pendingRunnables.remove(it.searchHistoryItem)
							if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
							searchHistoryItemsPendingRemoval.remove(it)
							dispatch(UserIntent.CancelPendingRemoval(it))
						}.launchIn(lifecycleScope)
					} else {
						favouritesAdapter.updateItems(favourites)
					}
				}

			}
		}
	}

	//endregion

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setFragmentResultListener(DELETE_IN_COMPANY_KEY) { _, bundle ->
			val isCompanyDeleted = bundle.getBoolean(DELETE_IN_COMPANY_BUNDLE_KEY)
			if (isCompanyDeleted) {
				lifecycleScope.launch {
					delay(STARTUP_DELAY)
					dispatch(UserIntent.DeletedInCompany)
				}
			}
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		viewModel.onViewCreated(this, viewLifecycleOwner.essentyLifecycle())
		initializeToolBar()
		createRecyclerView()
		setUpItemTouchHelper()
		setUpAnimationDecoratorHelper()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabFavourites.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabFavourites.setNavigationOnClickListener { findNavController().popBackStack() }
		toolBar?.setTitle(R.string.favourites)

	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvFavourites.layoutManager = linearLayoutManager
		binding.rvFavourites.addItemDecoration(DividerItemDecoration(requireContext()))
	}

	//endregion

	//region Swipe to dismiss

	private val searchHistoryItemsPendingRemoval = ArrayList<FavouritesItem>()
	private val handler = Handler(Looper.getMainLooper())
	private val pendingRunnables = HashMap<SearchHistoryItem, Runnable>()

	fun pendingRemoval(item: FavouritesItem, position: Int) {
		if (!searchHistoryItemsPendingRemoval.contains(item)) {
			dispatch(UserIntent.InitPendingRemoval(item))
			searchHistoryItemsPendingRemoval.add(item)
			// this will redraw row in "undo" state
			favouritesAdapter.notifyItemChanged(position)
			val pendingRemovalRunnable = Runnable {
				if (searchHistoryItemsPendingRemoval.contains(item)) {
					searchHistoryItemsPendingRemoval.remove(item)
				}
				dispatch(UserIntent.RemoveItem(item))
			}
			handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
			pendingRunnables[item.searchHistoryItem] = pendingRemovalRunnable
		}
	}

	private fun setUpItemTouchHelper() {

		val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

			// we want to cache these and not allocate anything repeatedly in the onChildDraw method
			lateinit var background: Drawable
			lateinit var xMark: Drawable
			var xMarkMargin: Int = 0
			var initiated: Boolean = false

			private fun init() {
				background = ColorDrawable(Color.RED)
				xMark = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)!!
				//Set it to background color, because otherwise it will stay on top after half swipe
				val a = TypedValue()
				requireActivity().theme.resolveAttribute(android.R.attr.windowBackground, a, true)
				val color = a.data
				xMark.colorFilter = BlendModeColorFilterCompat
					.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
				xMarkMargin = this@FavouritesFragment.resources.getDimension(R.dimen.viewMargin).toInt()
				initiated = true
			}

			// not important, we don't want drag & drop
			override fun onMove(
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				target: RecyclerView.ViewHolder
			): Boolean {
				return false
			}

			override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
				val listItem = (viewHolder as FavouritesViewHolder).favouritesItem
				return if (searchHistoryItemsPendingRemoval.contains(listItem)) {
					0
				} else super.getSwipeDirs(recyclerView, viewHolder)
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
				val item = (viewHolder as FavouritesViewHolder).favouritesItem
				pendingRemoval(item, viewHolder.bindingAdapterPosition)
			}

			override fun onChildDraw(
				c: Canvas,
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				dX: Float,
				dY: Float,
				actionState: Int,
				isCurrentlyActive: Boolean
			) {
				val itemView = viewHolder.itemView

				// not sure why, but this method get's called for viewholder that are already swiped away
				if (viewHolder.bindingAdapterPosition == -1) {
					// not interested in those
					return
				}

				if (!initiated) {
					init()
				}

				// draw red background
				background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
				background.draw(c)

				// draw x mark
				val itemHeight = itemView.bottom - itemView.top
				val intrinsicWidth = xMark.intrinsicWidth
				val intrinsicHeight = xMark.intrinsicWidth

				val xMarkLeft = itemView.right - xMarkMargin - intrinsicWidth
				val xMarkRight = itemView.right - xMarkMargin
				val xMarkTop = itemView.top + (itemHeight - intrinsicHeight) / 2
				val xMarkBottom = xMarkTop + intrinsicHeight
				xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom)

				xMark.draw(c)

				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
			}

		}
		val mItemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
		mItemTouchHelper.attachToRecyclerView(binding.rvFavourites)
	}

	private fun setUpAnimationDecoratorHelper() {
		binding.rvFavourites.addItemDecoration(object : RecyclerView.ItemDecoration() {

			// we want to cache this and not allocate anything repeatedly in the onDraw method
			lateinit var background: Drawable
			var initiated: Boolean = false

			private fun init() {
				background = ColorDrawable(Color.RED)
				initiated = true
			}

			override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

				if (!initiated) {
					init()
				}

				// only if animation is in progress
				if (parent.itemAnimator?.isRunning == true) {

					// some items might be animating down and
					// some items might be animating up to close the gap left by the removed item
					// this is not exclusive, both movement can be happening at the same time
					// to reproduce this leave just enough items so the first one
					// and the last one would be just a little off screen
					// then remove one from the middle

					// find first child with translationY > 0
					// and last one with translationY < 0
					// we're after a rect that is not covered in recycler-view views at this point in time
					var lastViewComingDown: View? = null
					var firstViewComingUp: View? = null

					// this is fixed
					val left = 0
					val right = parent.width

					// this we need to find out
					var top = 0
					var bottom = 0

					// find relevant translating views
					parent.layoutManager?.let {
						val childCount = it.childCount
						for (i in 0 until childCount) {
							val child = it.getChildAt(i)
							child?.let {
								if (child.translationY < 0) {
									// view is coming down
									lastViewComingDown = child
								} else if (child.translationY > 0) {
									// view is coming up
									if (firstViewComingUp == null) {
										firstViewComingUp = child
									}
								}
							}
						}
					}

					if (lastViewComingDown != null && firstViewComingUp != null) {
						// views are coming down AND going up to fill the void
						lastViewComingDown?.let {
							top = it.bottom + it.translationY.toInt()
						}
						firstViewComingUp?.let {
							bottom = it.top + it.translationY.toInt()
						}
					} else if (lastViewComingDown != null) {
						// views are going down to fill the void
						lastViewComingDown?.let {
							top = it.bottom + it.translationY.toInt()
							bottom = it.bottom
						}
					} else if (firstViewComingUp != null) {
						// views are coming up to fill the void
						firstViewComingUp?.let {
							top = it.top
							bottom = it.top + it.translationY.toInt()
						}
					}

					background.setBounds(left, top, right, bottom)
					background.draw(c)

				}
				super.onDraw(c, parent, state)
			}

		})
	}

	//endregion
}

sealed class UserIntent {
	data class FavouritesItemClicked(val favouritesItem: FavouritesItem) : UserIntent()
	data class InitPendingRemoval(val favouritesItem: FavouritesItem) : UserIntent()
	data class RemoveItem(val favouritesItem: FavouritesItem) : UserIntent()
	data class CancelPendingRemoval(val favouritesItem: FavouritesItem) : UserIntent()
	object DeletedInCompany : UserIntent()
}

sealed class SideEffect {
	data class FavouritesItemClicked(val selectedCompanyNumber: String, val companyName: String) : SideEffect()
}
