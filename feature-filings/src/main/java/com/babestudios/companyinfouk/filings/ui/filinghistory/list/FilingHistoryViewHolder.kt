package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.databinding.RowFilingHistoryBinding
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription

class FilingHistoryViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(filingHistoryItem: FilingHistoryItem) {
		val binding = rawBinding as RowFilingHistoryBinding
		val spannableDescription = filingHistoryItem.description.createSpannableDescription()
		binding.lblFilingHistoryDescription.text = spannableDescription
		binding.lblFilingHistoryDate.text = filingHistoryItem.date
		binding.lblFilingHistoryCategory.text = filingHistoryItem.category.displayName
		binding.lblFilingHistoryType.text = filingHistoryItem.type
	}
}
