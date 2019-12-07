package com.babestudios.companyinfouk.charges.ui.details.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.data.model.charges.Transaction
import java.lang.IllegalStateException

class ChargeDetailsTypeFactory : ChargeDetailsAdapter.ChargeDetailsTypeFactory {
	override fun type(chargesItem: Transaction): Int = R.layout.row_charge_details_transaction
	override fun type(chargeDetailsHeaderItem: ChargeDetailsHeaderItem): Int = R.layout.row_charge_details_header

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_charge_details_transaction -> ChargeDetailsViewHolder(view)
			R.layout.row_charge_details_header -> ChargeDetailsHeaderViewHolder(view)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
