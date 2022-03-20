package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.databinding.RowFilingHistoryBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class FilingHistoryAdapter internal constructor(
		private var visitables: List<FilingHistoryVisitable>,
		private val filingHistoryTypeFactory: FilingHistoryTypeFactory
) : RecyclerView.Adapter<BaseViewHolder<FilingHistoryVisitable>>() {

	interface FilingHistoryTypeFactory {
		fun type(filingHistoryItem: FilingHistoryItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<FilingHistoryVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<FilingHistoryVisitableBase> {
		val binding = RowFilingHistoryBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = filingHistoryTypeFactory.holder(viewType, binding)
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(viewHolder: BaseViewHolder<FilingHistoryVisitable>, position: Int) {
		viewHolder.bind(visitables[position])
	}

	override fun getItemViewType(position: Int): Int {
		return visitables[position].type(filingHistoryTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<FilingHistoryVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<FilingHistoryVisitable>> {
		return itemClickSubject
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return visitables.size
	}

	fun updateItems(visitables: List<FilingHistoryVisitable>) {
		this.visitables = visitables
		notifyDataSetChanged()
	}
}
