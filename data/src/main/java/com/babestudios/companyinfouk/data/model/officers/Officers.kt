package com.babestudios.companyinfouk.data.model.officers


import com.babestudios.companyinfouk.data.model.common.SelfLinkData
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Officers (
		@SerializedName("kind")
	var kind: String? = null,
		@SerializedName("items_per_page")
	var itemsPerPage: Int = 0,
		@SerializedName("total_results")
	var totalResults: Int = 0,
		@SerializedName("active_count")
	var activeCount: Int = 0,
		@SerializedName("start_index")
	var startIndex: Int = 0,
		@SerializedName("etag")
	var etag: String? = null,
		@SerializedName("items")
	var items: List<OfficerItem> = ArrayList(),
		@SerializedName("resigned_count")
	var resignedCount: Int = 0,
		@SerializedName("links")
	var links: SelfLinkData? = null

)
