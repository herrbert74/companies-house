package com.babestudios.companyinfouk.companies.ui.main.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchAdapter(private var searchVisitables: List<AbstractSearchVisitable>
					, private val searchTypeFactory: SearchTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractSearchVisitable>>() {

	override fun getItemCount(): Int {
		return searchVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return searchVisitables[position].type(searchTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractSearchVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractSearchVisitable>> {
		return itemClickSubject
	}

	interface SearchTypeFactory {
		fun type(searchItem: CompanySearchResultItem): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractSearchVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = searchTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractSearchVisitable>
		RxView.clicks(view)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractSearchVisitable>, position: Int) {
		holder.bind(searchVisitables[position])
	}

	fun updateItems(visitables: List<AbstractSearchVisitable>) {
		searchVisitables = visitables
		notifyDataSetChanged()
	}
}
