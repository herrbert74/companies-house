package com.babestudios.companyinfouk.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife

class RecentSearchesResultsAdapter(c: Context, searchHistoryItems: ArrayList<SearchHistoryItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private val mItemListener: RecentSearchesRecyclerViewClickListener

	private var searchHistoryItems = ArrayList<SearchHistoryItem>()

	init {
		mItemListener = c as RecentSearchesRecyclerViewClickListener
		this.searchHistoryItems = searchHistoryItems
	}

	fun refreshData(searchHistoryItems: ArrayList<SearchHistoryItem>) {
		this.searchHistoryItems = searchHistoryItems
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		if (viewType == TYPE_ITEM) {
			val itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.row_recent_searches, parent, false)

			return RecentSearchesViewHolder(itemLayoutView)
		} else if (viewType == TYPE_HEADER) {
			val itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.row_recent_searches_title, parent, false)

			return TitleViewHolder(itemLayoutView)
		}
		throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
	}

	override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
		if (viewHolder is RecentSearchesViewHolder) {
			viewHolder.lblCompanyName!!.text = searchHistoryItems[searchHistoryItems.size - position].companyName
			viewHolder.lblCompanyNumber!!.text = searchHistoryItems[searchHistoryItems.size - position].companyNumber
		}
	}

	override fun getItemViewType(position: Int): Int {
		return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM

	}

	private fun isPositionHeader(position: Int): Boolean {
		return position == 0
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return searchHistoryItems.size + 1
	}

	internal inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		@JvmField
		@BindView(R.id.lblTitle)
		var lblTitle: TextView? = null
	}


	internal inner class RecentSearchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblCompanyName)
		var lblCompanyName: TextView? = null
		@JvmField
		@BindView(R.id.lblCompanyNumber)
		var lblCompanyNumber: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			mItemListener.recentSearchesResultItemClicked(v, this.layoutPosition, searchHistoryItems[searchHistoryItems.size - layoutPosition].companyName, searchHistoryItems[searchHistoryItems.size - layoutPosition].companyNumber)
		}
	}

	internal interface RecentSearchesRecyclerViewClickListener {
		fun recentSearchesResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String)
	}

	companion object {

		private const val TYPE_HEADER = 0
		private const val TYPE_ITEM = 1
	}
}
