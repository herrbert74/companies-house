package com.babestudios.companyinfouk.ui.insolvencydetails.list

import android.view.View
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.list.BaseViewHolder

class InsolvencyDetailsTypeFactory : InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory {
	override fun type(insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem): Int = R.layout.row_recent_searches_title
	override fun type(insolvencyDetailsDateItem: InsolvencyDetailsDateItem): Int = R.layout.row_insolvency_details_date
	override fun type(insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem): Int = R.layout.row_insolvency_details_practitioner

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_recent_searches_title -> InsolvencyDetailsTitleViewHolder(view)
			R.layout.row_insolvency_details_date -> InsolvencyDetailsDateViewHolder(view)
			R.layout.row_insolvency_details_practitioner -> InsolvencyDetailsPractitionerViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}