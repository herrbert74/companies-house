package com.babestudios.companyinfouk.charges.ui.charges.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_charges.view.*

class ChargesViewHolder(itemView: View) : BaseViewHolder<AbstractChargesVisitable>(itemView) {
	override fun bind(item: AbstractChargesVisitable) {
		val chargesItem = (item as ChargesVisitable).chargesItem
		itemView.lblRowChargesCreatedOn.text = chargesItem.createdOn
		itemView.lblRowChargesChargeCode.text = chargesItem.chargeCode
		itemView.lblRowChargesStatus.text = chargesItem.status
		itemView.lblRowChargesPersonEntitled.text = chargesItem.personsEntitled[0].name
	}
}
