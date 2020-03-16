package com.babestudios.companyinfouk.charges.ui.charges.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

class ChargesTypeFactory : ChargesAdapter.ChargesTypeFactory {
	override fun type(chargesItem: ChargesItem): Int = R.layout.row_charges

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_charges -> ChargesViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
