package com.babestudios.companyinfouk.charges.ui.charges.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.data.model.charges.ChargesItem

class ChargesTypeFactory : ChargesAdapter.ChargesTypeFactory {
	override fun type(chargesItem: ChargesItem): Int = R.layout.row_charges

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_charges -> ChargesViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}