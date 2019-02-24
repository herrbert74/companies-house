package com.babestudios.companyinfouk.ui.insolvency.list

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase

class InsolvencyTypeFactory : InsolvencyAdapter.InsolvencyTypeFactory {
	override fun type(insolvencyCase: InsolvencyCase): Int = R.layout.row_insolvency

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_insolvency -> InsolvencyViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}