package com.babestudios.companyinfouk.data.model.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CompanySearchResult {

	@SerializedName("total_results")
	public Integer totalResults;

	@SerializedName("items")
	public List<CompanySearchResultItem> items = new ArrayList<>();

}
