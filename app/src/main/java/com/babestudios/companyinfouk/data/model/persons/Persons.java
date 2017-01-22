package com.babestudios.companyinfouk.data.model.persons;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Persons {
	@SerializedName("start_index")
	public Long startIndex;
	@SerializedName("links")
	public Links links;
	@SerializedName("active_count")
	public Long activeCount;
	@SerializedName("items")
	public List<Person> items = new ArrayList<>();
	@SerializedName("ceased_count")
	public Long ceasedCount;
	@SerializedName("items_per_page")
	public Long itemsPerPage;
	@SerializedName("total_results")
	public Long totalResults;
}
