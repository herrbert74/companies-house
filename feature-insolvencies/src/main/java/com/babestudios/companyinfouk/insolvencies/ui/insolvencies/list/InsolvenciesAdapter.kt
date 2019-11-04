package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase

class InsolvenciesAdapter(private var insolvencyVisitables: List<AbstractInsolvencyVisitable>
						  , private val insolvencyTypeFactory: InsolvencyTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractInsolvencyVisitable>>() {

	override fun getItemCount(): Int {
		return insolvencyVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return insolvencyVisitables[position].type(insolvencyTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractInsolvencyVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractInsolvencyVisitable>> {
		return itemClickSubject
	}

	interface InsolvencyTypeFactory {
		fun type(insolvencyCase: InsolvencyCase): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractInsolvencyVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = insolvencyTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractInsolvencyVisitable>
		RxView.clicks(view)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractInsolvencyVisitable>, position: Int) {
		holder.bind(insolvencyVisitables[position])
	}

	fun updateItems(visitables: List<AbstractInsolvencyVisitable>) {
		insolvencyVisitables = visitables
		notifyDataSetChanged()
	}
}