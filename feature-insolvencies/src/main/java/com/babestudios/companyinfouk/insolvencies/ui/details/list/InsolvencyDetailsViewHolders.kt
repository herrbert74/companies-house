package com.babestudios.companyinfouk.insolvencies.ui.details.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.common.databinding.RowSubtitleBinding
import com.babestudios.companyinfouk.common.databinding.RowTwoLinesBinding
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyDetailsPractitionerBinding

class InsolvencyDetailsTitleViewHolder(_binding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowSubtitleBinding
		val insolvencyDetailsTitleItem = (visitable as InsolvencyDetailsTitleVisitable).insolvencyDetailsTitleItem
		binding.lblCommonSubtitle.text = insolvencyDetailsTitleItem.title
	}
}

class InsolvencyDetailsDateViewHolder(_binding: ViewBinding) 
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowTwoLinesBinding
		val insolvencyDetailsDateItem = 
				(visitable as InsolvencyDetailsDateVisitable).insolvencyDetailsDateItem
		binding.lblCommonTwoLinesTitle.text = insolvencyDetailsDateItem.date
		binding.lblCommonTwoLinesText.text = insolvencyDetailsDateItem.type
	}
}

class InsolvencyDetailsPractitionerViewHolder(_binding: ViewBinding)
	: BaseViewHolder<InsolvencyDetailsVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyDetailsVisitableBase) {
		val binding = _binding as RowInsolvencyDetailsPractitionerBinding
		val insolvencyDetailsPractitionerItem =
				(visitable as InsolvencyDetailsPractitionerVisitable)
						.insolvencyDetailsPractitionerItem
		binding.lblInsolvencyDetailsPractitionerName.text = insolvencyDetailsPractitionerItem.practitioner.name
	}
}
