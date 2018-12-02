package com.babestudios.companyinfouk.data.model.persons


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Persons {
	@SerializedName("start_index")
	var startIndex: Long? = null
	@SerializedName("links")
	var links: Links? = null
	@SerializedName("active_count")
	var activeCount: Long? = null
	@SerializedName("items")
	var items: List<Person> = ArrayList()
	@SerializedName("ceased_count")
	var ceasedCount: Long? = null
	@SerializedName("items_per_page")
	var itemsPerPage: Long? = null
	@SerializedName("total_results")
	var totalResults: Long? = null
}
