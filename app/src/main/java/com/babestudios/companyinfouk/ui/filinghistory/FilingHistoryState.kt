package com.babestudios.companyinfouk.ui.filinghistory

import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.data.model.filinghistory.Category
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem


enum class ContentChange {
	NONE,
	SHOW_FILING_HISTORY_ITEM
}

data class FilingHistoryState(
		var filingHistoryList: MutableList<FilingHistoryVisitable> = ArrayList()
) : BaseState() {
	var page: Int? = null
	var perPage: Int? = null
	var total: Int? = null
	var contentChange: ContentChange = ContentChange.NONE
	var companyNumber: String? = null
	var filingCategoryFilter: Category = Category.CATEGORY_SHOW_ALL
	var clickedFilingHistoryItem: FilingHistoryItem? = null
}