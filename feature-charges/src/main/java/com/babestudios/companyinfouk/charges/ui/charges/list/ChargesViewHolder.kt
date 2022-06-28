package com.babestudios.companyinfouk.charges.ui.charges.list

import android.view.View.GONE
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.ext.colorStateList
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.RowChargesBinding
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem

class ChargesViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(chargesItem: ChargesItem) {
		val binding = rawBinding as RowChargesBinding
		binding.lblRowChargesCreatedOn.text = chargesItem.createdOn
		if (chargesItem.chargeCode.isEmpty()) {
			binding.lblRowChargesChargeCode.visibility = GONE
		} else {
			binding.lblRowChargesChargeCode.text = chargesItem.chargeCode
		}
		if (chargesItem.status.equals("satisfied", true)) {
			binding.ivRowChargesStatus.setImageResource(R.drawable.ic_baseline_sentiment_satisfied)
			ImageViewCompat.setImageTintList(binding.ivRowChargesStatus, itemView.context.colorStateList(R.color.green))
		} else {
			binding.ivRowChargesStatus.setImageResource(R.drawable.ic_baseline_sentiment_very_dissatisfied)
			ImageViewCompat.setImageTintList(binding.ivRowChargesStatus, itemView.context.colorStateList(R.color.red))
		}
		binding.lblRowChargesPersonEntitled.text = chargesItem.personsEntitled
	}
}
