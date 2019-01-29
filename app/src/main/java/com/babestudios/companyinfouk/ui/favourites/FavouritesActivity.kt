package com.babestudios.companyinfouk.ui.favourites

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.TypedValue
import android.view.View

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.company.CompanyActivity
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin
import com.babestudios.base.view.DividerItemDecoration
import com.pascalwelsch.compositeandroid.activity.CompositeActivity

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin

import java.util.ArrayList
import java.util.Arrays

import javax.inject.Inject

import kotlinx.android.synthetic.main.activity_favourites.*

class FavouritesActivity : CompositeActivity(), FavouritesActivityView, FavouritesAdapter.FavouritesRecyclerViewClickListener {

	private var favouritesAdapter: FavouritesAdapter? = null

	@Inject
	lateinit var favouritesPresenter: FavouritesPresenter

	private var favouritesActivityPlugin = TiActivityPlugin<FavouritesPresenter, FavouritesActivityView> {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		favouritesPresenter
	}

	internal var baseActivityPlugin = BaseActivityPlugin()

	init {

		addPlugin(favouritesActivityPlugin)
		addPlugin(baseActivityPlugin)
	}

	override fun onCreate(savedInstanceState: Bundle?) {

		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_favourites)
		baseActivityPlugin.logScreenView(this.localClassName)

		if (tbFavourites != null) {
			setSupportActionBar(tbFavourites)
			supportActionBar?.setDisplayHomeAsUpEnabled(true)
			tbFavourites?.setNavigationOnClickListener { onBackPressed() }
		}
		createRecentSearchesRecyclerView()
		setUpItemTouchHelper()
		setUpAnimationDecoratorHelper()
	}

	private fun createRecentSearchesRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvFavourites?.layoutManager = linearLayoutManager
		rvFavourites?.addItemDecoration(
				DividerItemDecoration(this))
	}


	override fun showProgress() {
		pbFavourites?.visibility = View.VISIBLE
	}

	override fun hideProgress() {
		pbFavourites?.visibility = View.GONE
	}

	override fun showFavourites(searchHistoryItems: Array<SearchHistoryItem>?) {
		rvFavourites?.visibility = View.VISIBLE
		val searchHistoryItemsList: ArrayList<SearchHistoryItem> = if (searchHistoryItems != null) {
			ArrayList(Arrays.asList(*searchHistoryItems))
		} else {
			ArrayList()
		}
		if (rvFavourites?.adapter == null) {
			favouritesAdapter = FavouritesAdapter(this@FavouritesActivity, searchHistoryItemsList)
			rvFavourites?.adapter = favouritesAdapter
		} else {
			(rvFavourites?.adapter as FavouritesAdapter).updateAdapter(searchHistoryItemsList)
		}
	}

	override fun favouritesResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String) {
		favouritesActivityPlugin.presenter.getCompany(companyNumber, companyName)
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		favouritesActivityPlugin.presenter.removeFavourite(favouriteToRemove)
	}


	override fun startCompanyActivity(companyNumber: String, companyName: String) {
		val startIntent = Intent(this, CompanyActivity::class.java)
		startIntent.putExtra("companyNumber", companyNumber)
		startIntent.putExtra("companyName", companyName)
		baseActivityPlugin.startActivityWithRightSlide(startIntent)
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
				return if (favouritesAdapter?.isPendingRemoval(position) == true) {
					0
				} else super.getSwipeDirs(recyclerView, viewHolder)
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
				val swipedPosition = viewHolder.adapterPosition
				favouritesAdapter?.pendingRemoval(swipedPosition)
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

	override fun super_onBackPressed() {
		super.super_finish()
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

}
