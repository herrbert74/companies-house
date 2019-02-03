package com.babestudios.companyinfouk.ui.charges.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_charges.view.*

class ChargesViewHolder(itemView: View) : BaseViewHolder<AbstractChargesVisitable>(itemView) {
	override fun bind(item: AbstractChargesVisitable) {
		val chargesItem = (item as ChargesVisitable).chargesItem
		itemView.lblCreatedOn.text = chargesItem.createdOn
		itemView.lblChargeCode.text = chargesItem.chargeCode
		itemView.lblStatus.text = chargesItem.status
		itemView.lblPersonEntitled.text = chargesItem.personsEntitled[0].name
	}
}