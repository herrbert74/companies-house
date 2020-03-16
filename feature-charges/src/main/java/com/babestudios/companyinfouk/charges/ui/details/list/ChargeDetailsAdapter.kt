package com.babestudios.companyinfouk.charges.ui.details.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsHeaderBinding
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsTransactionBinding
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
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
			: BaseViewHolder<AbstractChargeDetailsVisitable> {
		val binding = when (viewType) {
			R.layout.row_charge_details_header -> RowChargeDetailsHeaderBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
			else -> RowChargeDetailsTransactionBinding.inflate(
					LayoutInflater.from(parent.context),
					parent,
					false)
		}
		return chargeDetailsTypeFactory.holder(viewType, binding) as BaseViewHolder<AbstractChargeDetailsVisitable>
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractChargeDetailsVisitable>, position: Int) {
		holder.bind(chargeDetailsVisitables[position])
	}

	fun updateItems(visitables: List<AbstractChargeDetailsVisitable>) {
		chargeDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}
