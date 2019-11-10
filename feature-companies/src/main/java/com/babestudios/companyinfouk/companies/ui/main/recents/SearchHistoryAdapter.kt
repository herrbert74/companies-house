package com.babestudios.companyinfouk.companies.ui.main.recents

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem

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
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractSearchHistoryVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = searchHistoryTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractSearchHistoryVisitable>
		RxView.clicks(view)
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