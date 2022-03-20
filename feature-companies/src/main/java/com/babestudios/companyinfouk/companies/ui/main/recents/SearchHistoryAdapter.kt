package com.babestudios.companyinfouk.companies.ui.main.recents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchHistoryAdapter(
		private var searchHistoryVisitables: List<SearchHistoryVisitableBase>,
		private val searchHistoryTypeFactory: SearchHistoryTypeFactory
) : RecyclerView.Adapter<BaseViewHolder<SearchHistoryVisitableBase>>() {

	override fun getItemCount(): Int {
		return searchHistoryVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return searchHistoryVisitables[position].type(searchHistoryTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<SearchHistoryVisitableBase>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<SearchHistoryVisitableBase>> {
		return itemClickSubject
	}

	interface SearchHistoryTypeFactory {
		fun type(searchHistoryItem: SearchHistoryItem): Int
		fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<SearchHistoryVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SearchHistoryVisitableBase> {
		val binding = when (viewType) {
			R.layout.row_two_lines -> RowTwoLinesBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
			else -> RowSubtitleBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)

		}
		val v = searchHistoryTypeFactory.holder(viewType, binding)
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.filter { it is SearchHistoryViewHolder }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<SearchHistoryVisitableBase>, position: Int) {
		holder.bind(searchHistoryVisitables[position])
	}

	fun updateItems(visitables: List<SearchHistoryVisitableBase>) {
		searchHistoryVisitables = visitables
		notifyDataSetChanged()
	}
}
