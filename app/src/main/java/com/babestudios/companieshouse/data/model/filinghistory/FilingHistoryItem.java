package com.babestudios.companieshouse.data.model.filinghistory;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FilingHistoryItem {
	@SerializedName("date")
	public String date;

	@SerializedName("type")
	public String type;

	@SerializedName("links")
	public FilingHistoryLinks links;

	@SerializedName("category")
	public String category;

	@SerializedName("action_date")
	public String actionDate;

	@SerializedName("description")
	public String description;

	@SerializedName("description_values")
	public DescriptionValues descriptionValues;

	@SerializedName("pages")
	public Integer pages;

	@SerializedName("barcode")
	public String barcode;

	@SerializedName("transaction_id")
	public String transactionId;

	@SerializedName("associated_filings")
	public List<AssociatedFiling> associatedFilings = new ArrayList<AssociatedFiling>();

	@SerializedName("paper_filed")
	public Boolean paperFiled;

}
