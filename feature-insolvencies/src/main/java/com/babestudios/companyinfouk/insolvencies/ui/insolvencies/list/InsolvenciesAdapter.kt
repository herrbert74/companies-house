package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class InsolvenciesAdapter(private var insolvencyVisitables: List<InsolvencyVisitableBase>
						  , private val insolvencyTypeFactory: InsolvencyTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<InsolvencyVisitableBase>>() {

	override fun getItemCount(): Int {
		return insolvencyVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return insolvencyVisitables[position].type(insolvencyTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<InsolvencyVisitableBase>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<InsolvencyVisitableBase>> {
		return itemClickSubject
	}

	interface InsolvencyTypeFactory {
		fun type(insolvencyCase: InsolvencyCase): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<InsolvencyVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<InsolvencyVisitableBase> {
		val binding = RowInsolvencyBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = insolvencyTypeFactory.holder(viewType, binding)
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<InsolvencyVisitableBase>, position: Int) {
		holder.bind(insolvencyVisitables[position])
	}

	fun updateItems(visitables: List<InsolvencyVisitableBase>) {
		insolvencyVisitables = visitables
		notifyDataSetChanged()
	}
}
