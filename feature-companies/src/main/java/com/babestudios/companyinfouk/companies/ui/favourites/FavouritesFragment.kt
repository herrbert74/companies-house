package com.babestudios.companyinfouk.companies.ui.favourites

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.companies.ui.favourites.list.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_favourites.*
import java.util.*

private const val PENDING_REMOVAL_TIMEOUT = 5000 // 5sec
private const val REQUEST_SHOW_FAVOURITE_COMPANY = 8028

class FavouritesFragment : BaseMvRxFragment() {

	private var favouritesAdapter: FavouritesAdapter? = null

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.companiesNavigator.popBackStack()
		}
	})

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(this, callback)
		return inflater.inflate(R.layout.fragment_favourites, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabFavourites.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabFavourites.setNavigationOnClickListener { viewModel.companiesNavigator.popBackStack() }
		toolBar?.setTitle(R.string.favourites)
		createRecyclerView()
		setUpItemTouchHelper()
		setUpAnimationDecoratorHelper()
		viewModel.loadFavourites()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			viewModel.loadFavourites()
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		rvFavourites?.layoutManager = linearLayoutManager
		rvFavourites.addItemDecoration(DividerItemDecoration(requireContext()))
	}

	//endregion

	//region render

	override fun invalidate() {
		val tvMsvError = msvFavourites.findViewById<TextView>(R.id.tvMsvError)
		val ivEmptyView = msvFavourites.findViewById<ImageView>(R.id.ivEmptyView)
		withState(viewModel) { state ->

			when (state.favouritesRequest) {
				is Loading -> msvFavourites.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvFavourites.viewState = VIEW_STATE_ERROR
					tvMsvError.text = state.favouritesRequest.error.message
				}
				is Success -> {
					if (state.favouriteItems.isEmpty()) {
						msvFavourites.viewState = VIEW_STATE_EMPTY
						ivEmptyView.setImageResource(R.drawable.ic_business_empty_favorites)
					}
					msvFavourites.viewState = VIEW_STATE_CONTENT
					if (rvFavourites?.adapter == null) {
						favouritesAdapter = FavouritesAdapter(state.favouriteItems, FavouritesTypeFactory())
						rvFavourites?.adapter = favouritesAdapter
						observeActions()
					} else {
						favouritesAdapter?.updateItems(state.favouriteItems)
						observeActions()
					}
				}
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		favouritesAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractFavouritesVisitable> ->
					withState(viewModel) { state ->
						state.favouriteItems.let { favouriteItems ->
							val item = favouriteItems[(view as FavouritesViewHolder).adapterPosition].favouritesItem.searchHistoryItem
							//TODO
							// startActivityForResultWithRightSlide(this.createCompanyIntent(item.companyNumber, item.companyName), REQUEST_SHOW_FAVOURITE_COMPANY)
						}
					}
				}
				?.let { eventDisposables.add(it) }

		favouritesAdapter?.getCancelClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractFavouritesVisitable> ->
					// user wants to undo the removal, let's cancel the pending task
					val visitable = (view as FavouritesViewHolder).favouritesVisitable
					val pendingRemovalRunnable = pendingRunnables[visitable]
					pendingRunnables.remove(visitable)
					if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
					searchHistoryItemsPendingRemoval.remove(visitable)
					// this will rebind the row in "normal" state
					withState(viewModel) { state ->
						state.favouriteItems.let {
							it[it.indexOf(visitable)].favouritesItem.isPendingRemoval = false
							favouritesAdapter?.notifyItemChanged(it.indexOf(visitable))
						}
					}
					observeActions()
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion

	//region Swipe to dismiss

	private val searchHistoryItemsPendingRemoval = ArrayList<FavouritesVisitable>()
	private val handler = Handler()
	private val pendingRunnables = HashMap<FavouritesVisitable, Runnable>()

	fun pendingRemoval(position: Int) {
		withState(viewModel) { state ->
			val item = state.favouriteItems[position]
			if (!searchHistoryItemsPendingRemoval.contains(item)) {
				item.favouritesItem.isPendingRemoval = true
				searchHistoryItemsPendingRemoval.add(item)
				// this will redraw row in "undo" state
				favouritesAdapter?.notifyItemChanged(position)
				// let's create, store and post a runnable to remove the item
				val pendingRemovalRunnable = Runnable {
					if (searchHistoryItemsPendingRemoval.contains(item)) {
						searchHistoryItemsPendingRemoval.remove(item)
					}
					viewModel.removeFavourite(item.favouritesItem.searchHistoryItem)
				}
				handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
				pendingRunnables[item] = pendingRemovalRunnable
			}
		}
	}

	fun isPendingRemoval(position: Int): Boolean {
		return withState(viewModel) { state ->
			state.favouriteItems.let {
				val item = it[position]
				searchHistoryItemsPendingRemoval.contains(item)
			}
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
				xMark.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
				xMarkMargin = this@FavouritesFragment.resources.getDimension(R.dimen.view_margin_small).toInt()
				initiated = true
			}

			// not important, we don't want drag & drop
			override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
				return false
			}

			override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
				val position = viewHolder.adapterPosition
				return if (isPendingRemoval(position)) {
					0
				} else super.getSwipeDirs(recyclerView, viewHolder)
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
				val swipedPosition = viewHolder.adapterPosition
				pendingRemoval(swipedPosition)
			}

			override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
				val itemView = viewHolder.itemView

				// not sure why, but this method get's called for viewholder that are already swiped away
				if (viewHolder.adapterPosition == -1) {
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
		mItemTouchHelper.attachToRecyclerView(rvFavourites)
	}

	private fun setUpAnimationDecoratorHelper() {
		rvFavourites?.addItemDecoration(object : RecyclerView.ItemDecoration() {

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

					// some items might be animating down and some items might be animating up to close the gap left by the removed item
					// this is not exclusive, both movement can be happening at the same time
					// to reproduce this leave just enough items so the first one and the last one would be just a little off screen
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

fun Context.createFavouritesIntent(): Intent {
	return Intent(this, FavouritesFragment::class.java)
}
