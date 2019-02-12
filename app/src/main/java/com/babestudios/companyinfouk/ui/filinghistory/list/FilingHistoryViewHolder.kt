package com.babestudios.companyinfouk.ui.filinghistory.list

import android.view.View
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.CompaniesRepository
import kotlinx.android.synthetic.main.row_filing_history.view.*
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.ui.filinghistory.FilingHistoryPresenter
import javax.inject.Inject

class FilingHistoryViewHolder(itemView: View) : BaseViewHolder<FilingHistoryVisitable>(itemView) {

	init {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
	}

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	override fun bind(visitable: FilingHistoryVisitable) {
		val filingHistoryItem = visitable.filingHistoryItem
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			itemView.lblDescription?.text = filingHistoryItem.descriptionValues?.description
		} else {
			filingHistoryItem.description?.let {
				val spannableDescription = FilingHistoryPresenter.createSpannableDescription(companiesRepository.filingHistoryLookup(it), filingHistoryItem)
				itemView.lblDescription?.text = spannableDescription
			}
		}
		itemView.lblDate?.text = filingHistoryItem.date
		itemView.lblCategory?.text = filingHistoryItem.category?.displayName
		itemView.lblType?.text = filingHistoryItem.type
	}
}