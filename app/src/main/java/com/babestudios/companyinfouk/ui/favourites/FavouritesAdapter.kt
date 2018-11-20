package com.babestudios.companyinfouk.ui.favourites

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import kotlinx.android.synthetic.main.favourites_list_item.view.*
import java.util.*

internal class FavouritesAdapter(c: Context, searchHistoryItems: ArrayList<SearchHistoryItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private val mItemListener: FavouritesRecyclerViewClickListener

	private var searchHistoryItems = ArrayList<SearchHistoryItem>()

	private val searchHistoryItemsPendingRemoval = ArrayList<SearchHistoryItem>()
	private val handler = Handler()
	private val pendingRunnables = HashMap<SearchHistoryItem, Runnable>()

	init {
		mItemListener = c as FavouritesRecyclerViewClickListener
		this.searchHistoryItems = searchHistoryItems
	}

	fun updateAdapter(searchHistoryItems: ArrayList<SearchHistoryItem>) {
		this.searchHistoryItems = searchHistoryItems
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.favourites_list_item, parent, false)

		return FavouritesViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
		val item = searchHistoryItems[position]

		if (searchHistoryItemsPendingRemoval.contains(item)) {
			// we need to show the "undo" state of the row
			viewHolder.itemView.setBackgroundColor(Color.RED)
			viewHolder.itemView.llFavourites?.visibility = View.INVISIBLE
			viewHolder.itemView.btnFavouritesUndo?.visibility = View.VISIBLE
			viewHolder.itemView.btnFavouritesUndo?.setOnClickListener {
				// user wants to undo the removal, let's cancel the pending task
				val pendingRemovalRunnable = pendingRunnables[item]
				pendingRunnables.remove(item)
				if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable)
				searchHistoryItemsPendingRemoval.remove(item)
				// this will rebind the row in "normal" state
				notifyItemChanged(searchHistoryItems.indexOf(item))
			}
		} else {
			viewHolder.itemView.lblFavouritesCompanyName?.text = searchHistoryItems[position].companyName
			viewHolder.itemView.lblFavouritesCompanyNumber?.text = searchHistoryItems[position].companyNumber
			viewHolder.itemView.btnFavouritesUndo?.visibility = View.GONE
			viewHolder.itemView.btnFavouritesUndo?.setOnClickListener(null)
		}
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return searchHistoryItems.size
	}

	internal inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.favouritesResultItemClicked(v, this.layoutPosition, searchHistoryItems[layoutPosition].companyName, searchHistoryItems[layoutPosition].companyNumber)
		}
	}

	internal interface FavouritesRecyclerViewClickListener {
		fun favouritesResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String)
		fun removeFavourite(favouriteToRemove: SearchHistoryItem)
	}

	fun pendingRemoval(position: Int) {
		val item = searchHistoryItems[position]
		if (!searchHistoryItemsPendingRemoval.contains(item)) {
			searchHistoryItemsPendingRemoval.add(item)
			// this will redraw row in "undo" state
			notifyItemChanged(position)
			// let's create, store and post a runnable to remove the item
			val pendingRemovalRunnable = Runnable {
				remove(searchHistoryItems.indexOf(item))
				mItemListener.removeFavourite(item)
			}
			handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
			pendingRunnables[item] = pendingRemovalRunnable
		}
	}

	private fun remove(position: Int) {
		val item = searchHistoryItems[position]
		if (searchHistoryItemsPendingRemoval.contains(item)) {
			searchHistoryItemsPendingRemoval.remove(item)
		}
		if (searchHistoryItems.contains(item)) {
			searchHistoryItems.removeAt(position)
			notifyItemRemoved(position)
		}
	}

	fun isPendingRemoval(position: Int): Boolean {
		val item = searchHistoryItems[position]
		return searchHistoryItemsPendingRemoval.contains(item)
	}

	companion object {
		private const val PENDING_REMOVAL_TIMEOUT = 5000 // 5sec
	}
}
