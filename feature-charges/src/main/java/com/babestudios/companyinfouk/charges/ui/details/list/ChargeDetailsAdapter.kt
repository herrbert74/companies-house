package com.babestudios.companyinfouk.charges.ui.details.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsHeaderBinding
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsTransactionBinding
import com.babestudios.companyinfouk.domain.model.charges.Transaction

class ChargeDetailsAdapter(
	private var chargeDetailsVisitables: List<ChargeDetailsVisitableBase>,
	private val chargeDetailsTypeFactory: ChargeDetailsTypeFactory
) : RecyclerView.Adapter<BaseViewHolder<ChargeDetailsVisitableBase>>() {

	override fun getItemCount(): Int {
		return chargeDetailsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return chargeDetailsVisitables[position].type(chargeDetailsTypeFactory)
	}

	interface ChargeDetailsTypeFactory {
		fun type(chargeDetailsHeaderItem: ChargeDetailsHeaderItem): Int
		fun type(chargesItem: Transaction): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<ChargeDetailsVisitableBase>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
		: BaseViewHolder<ChargeDetailsVisitableBase> {
		val binding = when (viewType) {
			R.layout.row_charge_details_header -> RowChargeDetailsHeaderBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
			else -> RowChargeDetailsTransactionBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false
			)
		}
		return chargeDetailsTypeFactory.holder(viewType, binding)
	}

	override fun onBindViewHolder(holder: BaseViewHolder<ChargeDetailsVisitableBase>, position: Int) {
		holder.bind(chargeDetailsVisitables[position])
	}

	fun updateItems(visitables: List<ChargeDetailsVisitableBase>) {
		chargeDetailsVisitables = visitables
		notifyDataSetChanged()
	}
}
