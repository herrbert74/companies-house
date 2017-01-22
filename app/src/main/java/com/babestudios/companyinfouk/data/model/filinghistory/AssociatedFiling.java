package com.babestudios.companyinfouk.data.model.filinghistory;


import com.google.gson.annotations.SerializedName;

public class AssociatedFiling {
	@SerializedName("description_values")
	public DescriptionValues descriptionValues;
	@SerializedName("type")
	public String type;
	@SerializedName("description")
	public String description;
	@SerializedName("data")
	public Data data;
	@SerializedName("date")
	public String date;
	@SerializedName("action_date")
	public Long actionDate;
	@SerializedName("original_description")
	public String originalDescription;
	@SerializedName("category")
	public String category;

}
