package com.babestudios.companyinfouk.ui.charges.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfo.data.model.charges.ChargesItem

class ChargesAdapter(private var chargesVisitables: List<AbstractChargesVisitable>
					 , private val chargesTypeFactory: ChargesTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractChargesVisitable>>() {

	override fun getItemCount(): Int {
		return chargesVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return chargesVisitables[position].type(chargesTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractChargesVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractChargesVisitable>> {
		return itemClickSubject
	}

	interface ChargesTypeFactory {
		fun type(chargesItem: ChargesItem): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractChargesVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = chargesTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractChargesVisitable>
		RxView.clicks(view)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractChargesVisitable>, position: Int) {
		holder.bind(chargesVisitables[position])
	}

	fun updateItems(visitables: List<AbstractChargesVisitable>) {
		chargesVisitables = visitables
		notifyDataSetChanged()
	}
}