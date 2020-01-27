package com.babestudios.companyinfouk.charges.ui.charges.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding

class ChargesViewHolder(itemView2: View) : BaseViewHolder<AbstractChargesVisitable>(itemView2) {
	private var _binding : RowChargesBinding? = null
	private val binding get() = _binding!!
	override fun bind(visitable: AbstractChargesVisitable) {
		_binding = RowChargesBinding.bind(itemView)
		val chargesItem = (visitable as ChargesVisitable).chargesItem
		binding.lblRowChargesCreatedOn.text = chargesItem.createdOn
		binding.lblRowChargesChargeCode.text = chargesItem.chargeCode
		binding.lblRowChargesStatus.text = chargesItem.status
		binding.lblRowChargesPersonEntitled.text = chargesItem.personsEntitled[0].name
	}
}
