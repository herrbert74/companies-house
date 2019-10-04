package com.babestudios.companyinfo.data.model.search

import java.util.ArrayList

import com.google.gson.annotations.SerializedName

class CompanySearchResult {

	@SerializedName("total_results")
	var totalResults: Int? = null

	@SerializedName("items")
	var items: MutableList<CompanySearchResultItem> = ArrayList()

}
