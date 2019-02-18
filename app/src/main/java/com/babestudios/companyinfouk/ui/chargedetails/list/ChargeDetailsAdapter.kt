package com.babestudios.companyinfouk.ui.chargedetails.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.charges.Transaction

class ChargeDetailsAdapter(private var chargeDetailsVisitables: List<AbstractChargeDetailsVisitable>
						   , private val chargeDetailsTypeFactory: ChargeDetailsTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractChargeDetailsVisitable>>() {

	override fun getItemCount(): Int {
		return chargeDetailsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return chargeDetailsVisitables[position].type(chargeDetailsTypeFactory)
	}

	interface ChargeDetailsTypeFactory {
		fun type(chargeDetailsHeaderItem: ChargeDetailsHeaderItem): Int
		fun type(chargesItem: Transaction): Int
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractChargeDetailsVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		return chargeDetailsTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractChargeDetailsVisitable>
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractChargeDetailsVisitable>, position: Int) {
		holder.bind(chargeDetailsVisitables[position])
	}

	fun updateItems(visitables: List<AbstractChargeDetailsVisitable>) {
		chargeDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}