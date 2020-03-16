package com.babestudios.companyinfouk.companies.ui.main.recents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchHistoryAdapter(private var searchHistoryVisitables: List<AbstractSearchHistoryVisitable>
						   , private val searchHistoryTypeFactory: SearchHistoryTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractSearchHistoryVisitable>>() {

	override fun getItemCount(): Int {
		return searchHistoryVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return searchHistoryVisitables[position].type(searchHistoryTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractSearchHistoryVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractSearchHistoryVisitable>> {
		return itemClickSubject
	}

	interface SearchHistoryTypeFactory {
		fun type(searchHistoryItem: SearchHistoryItem): Int
		fun type(searchHistoryHeaderItem: SearchHistoryHeaderItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractSearchHistoryVisitable> {
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
		val v = searchHistoryTypeFactory.holder(viewType, binding) as BaseViewHolder<AbstractSearchHistoryVisitable>
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractSearchHistoryVisitable>, position: Int) {
		holder.bind(searchHistoryVisitables[position])
	}

	fun updateItems(visitables: List<AbstractSearchHistoryVisitable>) {
		searchHistoryVisitables = visitables
		notifyDataSetChanged()
	}
}
