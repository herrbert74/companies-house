package com.babestudios.companyinfouk.charges.ui.details.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.common.model.charges.Transaction
import com.babestudios.companyinfouk.data.model.charges.TransactionDto

class ChargeDetailsTypeFactory : ChargeDetailsAdapter.ChargeDetailsTypeFactory {
	override fun type(chargesItem: Transaction): Int = R.layout.row_charge_details_transaction
	override fun type(chargeDetailsHeaderItem: ChargeDetailsHeaderItem): Int = R.layout.row_charge_details_header

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<ChargeDetailsVisitableBase> {
		return when (type) {
			R.layout.row_charge_details_transaction -> ChargeDetailsViewHolder(binding)
			R.layout.row_charge_details_header -> ChargeDetailsHeaderViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
