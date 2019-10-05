package com.babestudios.companyinfouk.ui.filinghistory.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_filing_history.view.*

class FilingHistoryViewHolder(itemView: View) : BaseViewHolder<FilingHistoryVisitable>(itemView) {

	/*init {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
	}

	@Inject
	lateinit var companiesRepository: CompaniesRepositoryContract*/

	override fun bind(visitable: FilingHistoryVisitable) {
		val filingHistoryItem = visitable.filingHistoryItem
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			itemView.lblFilingHistoryDescription?.text = filingHistoryItem.descriptionValues?.description
		} else {
			filingHistoryItem.description?.let {
				/*val spannableDescription = FilingHistoryPresenter.createSpannableDescription(companiesRepository.filingHistoryLookup(it), filingHistoryItem)
				itemView.lblFilingHistoryDescription?.text = spannableDescription*/
			}
		}
		itemView.lblFilingHistoryDate?.text = filingHistoryItem.date
		itemView.lblFilingHistoryCategory?.text = filingHistoryItem.category?.displayName
		itemView.lblFilingHistoryType?.text = filingHistoryItem.type
	}
}