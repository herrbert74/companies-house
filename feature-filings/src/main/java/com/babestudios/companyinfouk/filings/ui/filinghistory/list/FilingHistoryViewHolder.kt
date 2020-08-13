package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.filings.databinding.RowFilingHistoryBinding
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription

class FilingHistoryViewHolder(_binding: ViewBinding) : BaseViewHolder<FilingHistoryVisitableBase>(_binding) {

	override fun bind(visitable: FilingHistoryVisitableBase) {
		val binding = _binding as RowFilingHistoryBinding
		val filingHistoryItem = (visitable as FilingHistoryVisitable).filingHistoryItem
		val spannableDescription = filingHistoryItem.description.createSpannableDescription()
		binding.lblFilingHistoryDescription.text = spannableDescription
		binding.lblFilingHistoryDate.text = filingHistoryItem.date
		binding.lblFilingHistoryCategory.text = filingHistoryItem.category.displayName
		binding.lblFilingHistoryType.text = filingHistoryItem.type
	}
}
