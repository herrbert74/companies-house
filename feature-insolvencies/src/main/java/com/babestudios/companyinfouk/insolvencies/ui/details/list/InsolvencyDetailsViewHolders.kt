package com.babestudios.companyinfouk.insolvencies.ui.details.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsPractitionerBinding

class InsolvencyDetailsTitleViewHolder(private val rawBinding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(rawBinding) {

	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = rawBinding as RowSubtitleBinding
		val insolvencyDetailsTitleItem = (visitable as InsolvencyDetailsTitleVisitable).insolvencyDetailsTitleItem
		binding.lblCommonSubtitle.text = insolvencyDetailsTitleItem.title
	}

}

class InsolvencyDetailsDateViewHolder(private val rawBinding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(rawBinding) {

	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = rawBinding as RowTwoLinesBinding
		val insolvencyDetailsDateItem =
			(visitable as InsolvencyDetailsDateVisitable).insolvencyDetailsDateItem
		binding.lblCommonTwoLinesTitle.text = insolvencyDetailsDateItem.date
		binding.lblCommonTwoLinesText.text = insolvencyDetailsDateItem.type
	}

}

class InsolvencyDetailsPractitionerViewHolder(private val rawBinding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(rawBinding) {

	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = rawBinding as RowInsolvencyDetailsPractitionerBinding
		binding.lblInsolvencyDetailsPractitionerName.text =
			(visitable as InsolvencyDetailsPractitionerVisitable).practitioner.name
	}

}
