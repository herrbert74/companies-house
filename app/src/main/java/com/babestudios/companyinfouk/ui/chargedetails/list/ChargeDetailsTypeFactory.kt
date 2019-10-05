package com.babestudios.companyinfouk.ui.chargedetails.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.charges.Transaction

class ChargeDetailsTypeFactory : ChargeDetailsAdapter.ChargeDetailsTypeFactory {
	override fun type(chargesItem: Transaction): Int = R.layout.row_charge_details_transaction
	override fun type(chargeDetailsHeaderItem: ChargeDetailsHeaderItem): Int = R.layout.row_charge_details_header

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_charge_details_transaction -> ChargeDetailsViewHolder(view)
			R.layout.row_charge_details_header -> ChargeDetailsHeaderViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}