package com.babestudios.companyinfouk.data.model.officers;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Officers {
	@SerializedName("kind")
	public String kind;
	@SerializedName("items_per_page")
	public int itemsPerPage;
	@SerializedName("total_results")
	public int totalResults;
	@SerializedName("active_count")
	public int activeCount;
	@SerializedName("start_index")
	public int startIndex;
	@SerializedName("etag")
	public String etag;
	@SerializedName("items")
	public List<OfficerItem> items = new ArrayList<>();
	@SerializedName("resigned_count")
	public int resignedCount;
	@SerializedName("links")
	public OfficersLinks links;

}
