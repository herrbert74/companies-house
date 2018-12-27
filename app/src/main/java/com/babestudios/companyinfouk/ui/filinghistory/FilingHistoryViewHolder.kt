package com.babestudios.companyinfouk.ui.filinghistory

import android.util.Log
import android.view.View
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.data.DataManager
import kotlinx.android.synthetic.main.filing_history_list_item.view.*
import net.medshr.android.base.mvp.lists.BaseViewHolder
import javax.inject.Inject

class FilingHistoryViewHolder(itemView: View) : BaseViewHolder<FilingHistoryVisitable>(itemView) {

	init {
		CompaniesHouseApplication.instance.applicationComponent.inject(this)
	}

	@Inject
	lateinit var dataManager: DataManager

	override fun bind(visitable: FilingHistoryVisitable) {
		val filingHistoryItem = visitable.filingHistoryItem
		if (filingHistoryItem.description == "legacy" || filingHistoryItem.description == "miscellaneous") {
			itemView.lblDescription?.text = filingHistoryItem.descriptionValues?.description
		} else {
			filingHistoryItem.description?.let {
				Log.d("test", "bind: $it")
				val spannableDescription = FilingHistoryPresenter.createSpannableDescription(dataManager.filingHistoryLookup(it), filingHistoryItem)
				itemView.lblDescription?.text = spannableDescription
			}
		}
		itemView.lblDate?.text = filingHistoryItem.date
		itemView.lblCategory?.text = filingHistoryItem.category?.displayName
		itemView.lblType?.text = filingHistoryItem.type
	}
}