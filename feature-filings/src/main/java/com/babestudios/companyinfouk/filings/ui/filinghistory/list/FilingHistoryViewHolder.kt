package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.filings.databinding.RowFilingHistoryBinding
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription

class FilingHistoryViewHolder(_binding: ViewBinding) : BaseViewHolder<FilingHistoryVisitable>(_binding) {

	override fun bind(visitable: FilingHistoryVisitable) {
		val binding = _binding as RowFilingHistoryBinding
		val filingHistoryItem = visitable.filingHistoryItem
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			binding.lblFilingHistoryDescription.text = filingHistoryItem.descriptionValues.description
		} else {
			val spannableDescription = filingHistoryItem.description.createSpannableDescription(filingHistoryItem)
			binding.lblFilingHistoryDescription.text = spannableDescription
		}
		binding.lblFilingHistoryDate.text = filingHistoryItem.date
		binding.lblFilingHistoryCategory.text = filingHistoryItem.category.displayName
		binding.lblFilingHistoryType.text = filingHistoryItem.type
	}
}
