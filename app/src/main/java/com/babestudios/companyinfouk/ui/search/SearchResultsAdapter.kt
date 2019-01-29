package com.babestudios.companyinfouk.ui.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem

import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.base.ext.biLet
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class SearchResultsAdapter internal constructor(c: Context, private val companySearchResults: CompanySearchResult, filterState: SearchPresenter.FilterState) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder>() {

	private val mItemListener: SearchResultsRecyclerViewClickListener

	private var filteredSearchResults: List<CompanySearchResultItem>? = null

	init {
		mItemListener = c as SearchResultsRecyclerViewClickListener
		updateFilteredResults(companySearchResults, filterState)
	}

	private fun updateFilteredResults(companySearchResults: CompanySearchResult, filterState: SearchPresenter.FilterState) {
		if (filterState.ordinal > SearchPresenter.FilterState.FILTER_SHOW_ALL.ordinal) {
			Observable.fromIterable(companySearchResults.items)
					.filter { companySearchResultItem -> companySearchResultItem.companyStatus != null && companySearchResultItem.companyStatus.equals(filterState.toString(), ignoreCase = true) }
					.observeOn(Schedulers.trampoline())
					.toList()
					.subscribe { result -> filteredSearchResults = result }
		} else {
			filteredSearchResults = companySearchResults.items
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, i: Int): SearchResultsViewHolder {
		val itemLayoutView = LayoutInflater.from(parent.context)
				.inflate(R.layout.row_search_result, parent, false)

		return SearchResultsViewHolder(itemLayoutView)
	}

	override fun onBindViewHolder(viewHolder: SearchResultsViewHolder, position: Int) {
		filteredSearchResults?.let {
			viewHolder.lblCompanyName?.text = it[position].title
			viewHolder.lblAddress?.text = it[position].addressSnippet
			viewHolder.lblActiveStatus?.text = it[position].companyStatus
			viewHolder.lblIncorporated?.text = it[position].description
		}
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return filteredSearchResults?.size ?: 0
	}

	internal fun setFilterOnAdapter(filterState: SearchPresenter.FilterState) {
		updateFilteredResults(companySearchResults, filterState)
		notifyDataSetChanged()
	}

	inner class SearchResultsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
		@JvmField
		@BindView(R.id.lblAddress)
		var lblAddress: TextView? = null
		@JvmField
		@BindView(R.id.lblCompanyName)
		var lblCompanyName: TextView? = null
		@JvmField
		@BindView(R.id.lblActiveStatus)
		var lblActiveStatus: TextView? = null
		@JvmField
		@BindView(R.id.lblIncorporated)
		var lblIncorporated: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			filteredSearchResults?.let {
				(it[layoutPosition].title to it[layoutPosition].companyNumber).biLet {title, companyNumber->
					mItemListener.searchResultItemClicked(v, this.layoutPosition, title, companyNumber)
				}
			}
		}
	}

	internal interface SearchResultsRecyclerViewClickListener {
		fun searchResultItemClicked(v: View, position: Int, companyName: String, companyNumber: String)
	}

	internal fun addItems(companySearchResults: CompanySearchResult, filterState: SearchPresenter.FilterState) {
		this.companySearchResults.items.addAll(companySearchResults.items)
		updateFilteredResults(companySearchResults, filterState)
		notifyDataSetChanged()
	}
}
