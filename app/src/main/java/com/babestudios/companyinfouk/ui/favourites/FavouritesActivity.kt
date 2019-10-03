package com.babestudios.companyinfouk.ui.favourites

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
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.ext.startActivityForResultWithRightSlide
import com.babestudios.companyinfouk.ui.company.createCompanyIntent
import com.babestudios.companyinfouk.ui.favourites.list.*
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.multi_state_view_empty.view.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*
import java.util.*

private const val PENDING_REMOVAL_TIMEOUT = 5000 // 5sec
private const val REQUEST_SHOW_FAVOURITE_COMPANY = 8028

class FavouritesActivity : RxAppCompatActivity(), ScopeProvider {

	private var favouritesAdapter: FavouritesAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(FavouritesViewModel::class.java) }

	lateinit var favouritesPresenter: FavouritesPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_favourites)
		logScreenView(this.localClassName)
		setSupportActionBar(pabFavourites.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabFavourites.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.favourites)
		when {
			viewModel.state.value.favouriteItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<FavouritesState>("STATE")?.let {
					with(viewModel.state.value) {
						favouriteItems = it.favouriteItems
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				initPresenter(viewModel)
			}
		}

		createRecyclerView()
		setUpItemTouchHelper()
		setUpAnimationDecoratorHelper()

		observeState()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (resultCode == Activity.RESULT_OK) {
			favouritesPresenter.loadFavourites()
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}


	private fun initPresenter(viewModel: FavouritesViewModel) {
		if (!::favouritesPresenter.isInitialized) {
			favouritesPresenter = Injector.get().favouritesPresenter()
			favouritesPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvFavourites?.layoutManager = linearLayoutManager
		rvFavourites.addItemDecoration(DividerItemDecoration(this))
	}

	override fun onBackPressed() {
		super.onBackPressed()
		super.overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: FavouritesState) {
		when {
			state.isLoading -> msvFavourites.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> {
				msvFavourites.viewState = VIEW_STATE_ERROR
				state.errorType = ErrorType.NONE
				msvFavourites.tvMsvError.text = state.errorMessage
			}
			state.favouriteItems?.isEmpty() == true -> {
				msvFavourites.viewState = VIEW_STATE_EMPTY
				msvFavourites.ivEmptyView.setImageResource(R.drawable.ic_business_empty_favorites)
			}
			else -> {
				state.favouriteItems?.let {
					msvFavourites.viewState = VIEW_STATE_CONTENT
					if (rvFavourites?.adapter == null) {
						favouritesAdapter = FavouritesAdapter(it, FavouritesTypeFactory())
						rvFavourites?.adapter = favouritesAdapter
						observeActions()
					} else {
						favouritesAdapter?.updateItems(it)
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
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractFavouritesVisitable> ->
					viewModel.state.value.favouriteItems?.let { favouriteItems ->
						val item = favouriteItems[(view as FavouritesViewHolder).adapterPosition].favouritesItem.searchHistoryItem
						startActivityForResultWithRightSlide(this.createCompanyIntent(item.companyNumber, item.companyName), REQUEST_SHOW_FAVOURITE_COMPANY)
					}
				}
				?.let { eventDisposables.add(it) }

		favouritesAdapter?.getCancelClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractFavouritesVisitable> ->
					// user wants to undo the removal, let's cancel the pending task
					val visitable = (view as FavouritesViewHolder).favouritesVisitable
					val pendingRemovalRunnable = pendingRunnables[visitable]
					pendingRunnables.remove(visitable)
					if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
					searchHistoryItemsPendingRemoval.remove(visitable)
					// this will rebind the row in "normal" state
					viewModel.state.value.favouriteItems?.let {
						it[it.indexOf(visitable)].favouritesItem.isPendingRemoval = false
						favouritesAdapter?.notifyItemChanged(it.indexOf(visitable))
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
		viewModel.state.value.favouriteItems?.let {
			val item = it[position]
			if (!searchHistoryItemsPendingRemoval.contains(item)) {
				item.favouritesItem.isPendingRemoval = true
				searchHistoryItemsPendingRemoval.add(item)
				// this will redraw row in "undo" state
				favouritesAdapter?.notifyItemChanged(position)
				// let's create, store and post a runnable to remove the item
				val pendingRemovalRunnable = Runnable {
					remove(it.indexOf(item))
					favouritesPresenter.removeFavourite(item.favouritesItem.searchHistoryItem)
				}
				handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
				pendingRunnables[item] = pendingRemovalRunnable
			}
		}
	}

	private fun remove(position: Int) {
		viewModel.state.value.favouriteItems?.let {
			val item = it[position]
			if (searchHistoryItemsPendingRemoval.contains(item)) {
				searchHistoryItemsPendingRemoval.remove(item)
			}
			if (it.contains(item)) {
				val list = it.toMutableList()
				list.removeAt(position)
				viewModel.state.value.favouriteItems = list.toList()
				favouritesAdapter?.updateItems(viewModel.state.value.favouriteItems!!)
			}
		}
	}

	fun isPendingRemoval(position: Int): Boolean {
		return viewModel.state.value.favouriteItems?.let {
			val item = it[position]
			searchHistoryItemsPendingRemoval.contains(item)
		} ?: false
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
				xMark = ContextCompat.getDrawable(this@FavouritesActivity, R.drawable.ic_close)!!
				//Set it to background color, because otherwise it will stay on top after half swipe
				val a = TypedValue()
				theme.resolveAttribute(android.R.attr.windowBackground, a, true)
				val color = a.data
				xMark.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
				xMarkMargin = this@FavouritesActivity.resources.getDimension(R.dimen.view_margin_small).toInt()
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
	return Intent(this, FavouritesActivity::class.java)
}
