package com.babestudios.companyinfouk.charges.ui.charges.list

import androidx.core.widget.ImageViewCompat
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.MultiStateView
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding
import com.babestudios.companyinfouk.common.ext.colorStateList

class ChargesViewHolder(_binding: ViewBinding)
	: BaseViewHolder<ChargesVisitableBase>(_binding) {

	override fun bind(visitable: ChargesVisitableBase) {
		val binding = _binding as RowChargesBinding
		val chargesItem = (visitable as ChargesVisitable).chargesItem
		binding.lblRowChargesCreatedOn.text = chargesItem.createdOn
		if (chargesItem.chargeCode.isNullOrEmpty()) {
			binding.lblRowChargesChargeCode.visibility = MultiStateView.GONE
		} else {
			binding.lblRowChargesChargeCode.text = chargesItem.chargeCode
		}
		if (chargesItem.status == "fully-satisfied") {
			binding.ivRowChargesStatus.setImageResource(R.drawable.ic_baseline_sentiment_satisfied)
			ImageViewCompat.setImageTintList(binding.ivRowChargesStatus, itemView.context.colorStateList(R.color.green))
		} else {
			binding.ivRowChargesStatus.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied)
			ImageViewCompat.setImageTintList(binding.ivRowChargesStatus, itemView.context.colorStateList(R.color.red))
		}
		binding.lblRowChargesPersonEntitled.text = chargesItem.personsEntitled[0].name
	}
}
