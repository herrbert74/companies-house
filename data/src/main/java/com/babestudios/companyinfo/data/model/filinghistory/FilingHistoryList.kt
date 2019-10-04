package com.babestudios.companyinfo.data.model.filinghistory


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class FilingHistoryList {
	@SerializedName("start_index")
	var startIndex: Int? = null
	@SerializedName("items_per_page")
	var itemsPerPage: Int? = null
	@SerializedName("items")
	var items: List<FilingHistoryItem> = ArrayList()
	@SerializedName("total_count")
	var totalCount: Int? = null
	@SerializedName("filing_history_status")
	var filingHistoryStatus: String? = null

}
