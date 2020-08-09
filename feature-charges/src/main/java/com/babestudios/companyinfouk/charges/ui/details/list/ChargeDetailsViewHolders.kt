package com.babestudios.companyinfouk.charges.ui.details.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsHeaderBinding
import com.babestudios.companyinfouk.charges.databinding.RowChargeDetailsTransactionBinding
import com.babestudios.companyinfouk.charges.ui.details.list.ChargeDetailsVisitableBase.*

class ChargeDetailsViewHolder(_binding: ViewBinding)
	: BaseViewHolder<ChargeDetailsVisitableBase>(_binding) {
	override fun bind(visitable: ChargeDetailsVisitableBase) {
		val binding = _binding as RowChargeDetailsTransactionBinding
		val chargeDetailsItem = (visitable as ChargeDetailsVisitable).transaction
		binding.tvChargeDetailsHeaderFilingType.text = chargeDetailsItem.filingType
		binding.tvChargeDetailsTransactionDeliveredOn.text = chargeDetailsItem.deliveredOn
	}
}

class ChargeDetailsHeaderViewHolder(_binding: ViewBinding)
	: BaseViewHolder<ChargeDetailsVisitableBase>(_binding) {
	override fun bind(visitable: ChargeDetailsVisitableBase) {
		val binding = _binding as RowChargeDetailsHeaderBinding
		val chargeDetailsHeaderItem = (visitable as ChargeDetailsHeaderVisitable)
				.chargeDetailsHeaderItem
		binding.cpnChargeDetailsHeaderTitle.text = chargeDetailsHeaderItem.title
	}
}
