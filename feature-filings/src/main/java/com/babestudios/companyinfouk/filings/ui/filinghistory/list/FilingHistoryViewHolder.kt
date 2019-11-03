package com.babestudios.companyinfouk.filings.ui.filinghistory.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.filings.ui.createSpannableDescription
import kotlinx.android.synthetic.main.row_filing_history.view.*

class FilingHistoryViewHolder(itemView: View) : BaseViewHolder<FilingHistoryVisitable>(itemView) {

	override fun bind(visitable: FilingHistoryVisitable) {
		val filingHistoryItem = visitable.filingHistoryItem
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			itemView.lblFilingHistoryDescription?.text = filingHistoryItem.descriptionValues.description
		} else {
			val spannableDescription = filingHistoryItem.description.createSpannableDescription(filingHistoryItem)
			itemView.lblFilingHistoryDescription?.text = spannableDescription
		}
		itemView.lblFilingHistoryDate?.text = filingHistoryItem.date
		itemView.lblFilingHistoryCategory?.text = filingHistoryItem.category.displayName
		itemView.lblFilingHistoryType?.text = filingHistoryItem.type
	}
}