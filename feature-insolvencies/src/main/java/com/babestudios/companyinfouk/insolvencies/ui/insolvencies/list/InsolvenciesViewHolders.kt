package com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.insolvencies.databinding.RowInsolvencyBinding

class InsolvencyViewHolder(_binding: ViewBinding) 
	: BaseViewHolder<InsolvencyVisitableBase>(_binding) {
	override fun bind(visitable: InsolvencyVisitableBase) {
		val binding = _binding as RowInsolvencyBinding
		val insolvencyCase = (visitable as InsolvencyVisitable).insolvencyCase
		binding.lblInsolvencyDate.text = insolvencyCase.dates[0].date
		binding.lblInsolvencyType.text = insolvencyCase.type
		if (insolvencyCase.practitioners.isNotEmpty()) {
			binding.lblInsolvencyPractitioner.text = insolvencyCase.practitioners[0].name
		}
	}
}
