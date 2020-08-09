package com.babestudios.companyinfouk.insolvencies.ui.details.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.insolvencies.R

class InsolvencyDetailsTypeFactory : InsolvencyDetailsAdapter.InsolvencyDetailsTypeFactory {
	override fun type(insolvencyDetailsTitleItem: InsolvencyDetailsTitleItem): Int =
			R.layout.row_subtitle
	override fun type(insolvencyDetailsDateItem: InsolvencyDetailsDateItem): Int =
			R.layout.row_insolvency_details_date
	override fun type(insolvencyDetailsPractitionerItem: InsolvencyDetailsPractitionerItem): Int =
			R.layout.row_insolvency_details_practitioner

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<InsolvencyDetailsVisitableBase> {
		return when (type) {
			R.layout.row_subtitle -> InsolvencyDetailsTitleViewHolder(binding)
			R.layout.row_insolvency_details_date ->
				InsolvencyDetailsDateViewHolder(binding)
			R.layout.row_insolvency_details_practitioner ->
				InsolvencyDetailsPractitionerViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
