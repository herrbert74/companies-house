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

class ChargesAdapter(private var chargesVisitables: List<ChargesVisitableBase>
					 , private val chargesTypeFactory: ChargesTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<ChargesVisitableBase>>() {

	override fun getItemCount(): Int {
		return chargesVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return chargesVisitables[position].type(chargesTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<ChargesVisitableBase>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<ChargesVisitableBase>> {
		return itemClickSubject
	}

	interface ChargesTypeFactory {
		fun type(chargesItem: ChargesItem): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<ChargesVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ChargesVisitableBase> {
		val binding = RowChargesBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = chargesTypeFactory.holder(viewType, binding)
		RxView.clicks(v.itemView)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ChargesVisitableBase>, position: Int) {
		holder.bind(chargesVisitables[position])
	}

	fun updateItems(visitables: List<ChargesVisitableBase>) {
		chargesVisitables = visitables
		notifyDataSetChanged()
	}
}
