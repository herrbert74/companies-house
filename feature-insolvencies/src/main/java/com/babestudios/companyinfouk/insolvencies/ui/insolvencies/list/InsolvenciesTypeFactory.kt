package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.R

class InsolvenciesTypeFactory : InsolvenciesAdapter.InsolvencyTypeFactory {
	override fun type(insolvencyCase: InsolvencyCase): Int = R.layout.row_insolvency

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<InsolvencyVisitableBase> {
		return when (type) {
			R.layout.row_insolvency -> InsolvencyViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
