package com.babestudios.companyinfouk.insolvencies.ui.details.list

import android.view.View
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.base.mvp.list.BaseViewHolder

class InsolvencyDetailsTypeFactory : InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory {
	override fun type(insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem): Int = R.layout.row_subtitle
	override fun type(insolvencyDetailsDateItem: InsolvencyDetailsDateItem): Int = R.layout.row_insolvency_details_date
	override fun type(insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem): Int = R.layout.row_insolvency_details_practitioner

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_subtitle -> InsolvencyDetailsTitleViewHolder(view)
			R.layout.row_insolvency_details_date -> InsolvencyDetailsDateViewHolder(view)
			R.layout.row_insolvency_details_practitioner -> InsolvencyDetailsPractitionerViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}