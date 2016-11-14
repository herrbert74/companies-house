package com.babestudios.companieshouse.data.model.charges;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Charges {
	@SerializedName("etag")
	public String etag;
	@SerializedName("items")
	public List<ChargesItem> items = new ArrayList<>();
	@SerializedName("part_satisfied_count")
	public String partSatisfiedCount;
	@SerializedName("satisfied_count")
	public String satisfiedCount;
	@SerializedName("total_count")
	public String totalCount;
	@SerializedName("unfiletered_count")
	public String unfileteredCount;
}
