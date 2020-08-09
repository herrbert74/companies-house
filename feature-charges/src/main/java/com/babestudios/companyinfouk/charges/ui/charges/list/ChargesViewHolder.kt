package com.babestudios.companyinfouk.charges.ui.charges.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding

class ChargesViewHolder(_binding: ViewBinding)
	: BaseViewHolder<ChargesVisitableBase>(_binding) {

	override fun bind(visitable: ChargesVisitableBase) {
		val binding = _binding as RowChargesBinding
		val chargesItem = (visitable as ChargesVisitable).chargesItem
		binding.lblRowChargesCreatedOn.text = chargesItem.createdOn
		binding.lblRowChargesChargeCode.text = chargesItem.chargeCode
		binding.lblRowChargesStatus.text = chargesItem.status
		binding.lblRowChargesPersonEntitled.text = chargesItem.personsEntitled[0].name
	}
}
