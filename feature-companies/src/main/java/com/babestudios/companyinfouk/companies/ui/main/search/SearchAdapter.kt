package com.babestudios.companyinfouk.companies.ui.main.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.companies.databinding.RowSearchResultBinding
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchAdapter(
	private var searchVisitables: List<SearchVisitableBase>,
	private val searchTypeFactory: SearchTypeFactory
) : RecyclerView.Adapter<BaseViewHolder<SearchVisitableBase>>() {

	override fun getItemCount(): Int {
		return searchVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return searchVisitables[position].type(searchTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<SearchVisitableBase>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<SearchVisitableBase>> {
		return itemClickSubject
	}

	interface SearchTypeFactory {
		fun type(searchItem: CompanySearchResultItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<SearchVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<SearchVisitableBase> {
		val binding = RowSearchResultBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false)
		val v = searchTypeFactory.holder(viewType, binding)
		RxView.clicks(binding.root)
			.takeUntil(RxView.detaches(parent))
			.map { v }
			.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<SearchVisitableBase>, position: Int) {
		holder.bind(searchVisitables[position])
	}

	fun updateItems(visitables: List<SearchVisitableBase>) {
		searchVisitables = visitables
		notifyDataSetChanged()
	}
}
