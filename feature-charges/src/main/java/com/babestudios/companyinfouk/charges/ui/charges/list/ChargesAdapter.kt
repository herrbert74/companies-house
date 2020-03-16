package com.babestudios.companyinfouk.charges.ui.charges.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

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
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractChargesVisitable> {
		val binding = RowChargesBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = chargesTypeFactory.holder(viewType, binding) as BaseViewHolder<AbstractChargesVisitable>
		RxView.clicks(v.itemView)
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
