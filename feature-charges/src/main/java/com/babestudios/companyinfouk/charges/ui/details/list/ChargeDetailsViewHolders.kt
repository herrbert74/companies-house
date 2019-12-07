package com.babestudios.companyinfouk.charges.ui.details.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_charge_details_header.view.*
import kotlinx.android.synthetic.main.row_charge_details_transaction.view.*

class ChargeDetailsViewHolder(itemView: View) : BaseViewHolder<AbstractChargeDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractChargeDetailsVisitable) {
		val chargeDetailsItem = (visitable as ChargeDetailsVisitable).transaction
		itemView.tvChargeDetailsHeaderFilingType.text = chargeDetailsItem.filingType
		itemView.tvChargeDetailsTransactionDeliveredOn.text = chargeDetailsItem.deliveredOn
	}
}

class ChargeDetailsHeaderViewHolder(itemView: View) : BaseViewHolder<AbstractChargeDetailsVisitable>(itemView) {
	override fun bind(visitable: AbstractChargeDetailsVisitable) {
		val chargeDetailsHeaderItem = (visitable as ChargeDetailsHeaderVisitable).chargeDetailsHeaderItem
		itemView.cpnChargeDetailsHeaderTitle.text = chargeDetailsHeaderItem.title
	}
}
