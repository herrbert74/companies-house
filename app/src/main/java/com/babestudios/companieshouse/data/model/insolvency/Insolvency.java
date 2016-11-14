package com.babestudios.companieshouse.data.model.insolvency;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Insolvency {
	@SerializedName("cases")
	public List<InsolvencyCase> cases = new ArrayList<>();
	@SerializedName("etag")
	public String etag;
	@SerializedName("status")
	public List<Object> status = new ArrayList<>();
}
