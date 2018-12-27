package com.babestudios.companyinfouk.ui.filinghistory

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.DataManager
import com.babestudios.companyinfouk.data.model.filinghistory.Category
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import net.medshr.android.base.mvp.lists.BaseViewHolder
import javax.inject.Inject

class FilingHistoryAdapter internal constructor(private var visitables: List<FilingHistoryVisitable>,
												private val filingHistoryTypesFactory: FilingHistoryTypesFactory) : RecyclerView.Adapter<BaseViewHolder<FilingHistoryVisitable>>() {


	interface FilingHistoryTypesFactory {
		fun type(filingHistoryItem: FilingHistoryItem): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	private var filteredFilingHistoryItems: List<FilingHistoryVisitable> = ArrayList()

	@Inject
	lateinit var dataManager: DataManager

	init {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
		updateFilteredResults()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FilingHistoryVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v =  filingHistoryTypesFactory.holder(viewType, view) as BaseViewHolder<FilingHistoryVisitable>
		RxView.clicks(view)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(viewHolder: BaseViewHolder<FilingHistoryVisitable>, position: Int) {
		viewHolder.bind(visitables[position])



	}

	override fun getItemViewType(position: Int): Int {
		return visitables[position].type(filingHistoryTypesFactory)
	}

	@SuppressLint("CheckResult")
	private fun updateFilteredResults() {
		/*if (categoryFilter.ordinal > Category.CATEGORY_SHOW_ALL.ordinal) {
			Observable.fromIterable(visitables).filter { filingHistoryVisitable -> filingHistoryVisitable.filingHistoryItem.category.equals(categoryFilter.toString(), ignoreCase = true) }
					.observeOn(Schedulers.trampoline())
					.toList()
					.subscribe { result -> filteredFilingHistoryItems = result }
		} else {*/
			filteredFilingHistoryItems = visitables
		//}
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<FilingHistoryVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<FilingHistoryVisitable>> {
		return itemClickSubject
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return filteredFilingHistoryItems.size
	}

	/*fun setFilterOnAdapter(categoryFilter: Category) {
		this.categoryFilter = categoryFilter
		updateFilteredResults()
		notifyDataSetChanged()
	}*/

	fun updateItems(visitables: List<FilingHistoryVisitable>) {
		this.visitables = visitables
		notifyDataSetChanged()
	}
}
