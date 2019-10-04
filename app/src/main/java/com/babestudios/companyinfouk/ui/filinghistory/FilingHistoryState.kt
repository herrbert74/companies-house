package com.babestudios.companyinfouk.ui.filinghistory

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfo.data.model.filinghistory.Category
import com.babestudios.companyinfo.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.ui.filinghistory.list.FilingHistoryVisitable
import kotlinx.android.parcel.Parcelize


enum class ContentChange {
	NONE,
	SHOW_FILING_HISTORY_ITEM
}

@Parcelize
data class FilingHistoryState(
		var filingHistoryList: MutableList<FilingHistoryVisitable>?,
		//TODO Test scenarios where these could be needed (save instance state)
		/*var page: Int? = null
		var perPage: Int? = null
		var total: Int? = null*/
		var contentChange: ContentChange = ContentChange.NONE,
		var companyNumber: String? = null,
		var filingCategoryFilter: Category = Category.CATEGORY_SHOW_ALL,
		var clickedFilingHistoryItem: FilingHistoryItem? = null
) : BaseState(), Parcelable