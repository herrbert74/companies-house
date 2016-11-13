package com.babestudios.companieshouse.data.model.filinghistory;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilingHistoryList {
	@SerializedName("start_index")
	public Integer startIndex;
	@SerializedName("items_per_page")
	public Integer itemsPerPage;
	@SerializedName("items")
	public List<FilingHistoryItem> items = new ArrayList<FilingHistoryItem>();
	@SerializedName("total_count")
	public Integer totalCount;
	@SerializedName("filing_history_status")
	public String filingHistoryStatus;

}
