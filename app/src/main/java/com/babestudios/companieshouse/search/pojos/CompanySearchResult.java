package com.babestudios.companieshouse.search.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CompanySearchResult {

	@SerializedName("total_results")
	public Integer totalResults;

	@SerializedName("items")
	public List<CompanySearchResultItem> items = new ArrayList<CompanySearchResultItem>();

}
