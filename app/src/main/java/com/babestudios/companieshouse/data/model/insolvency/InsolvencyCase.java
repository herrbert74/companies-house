package com.babestudios.companieshouse.data.model.insolvency;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InsolvencyCase {
	@SerializedName("dates")
	public List<Date> dates = new ArrayList<>();
	@SerializedName("links")
	public InsolvencyCaseLinks links;
	@SerializedName("notes")
	public List<String> notes = new ArrayList<>();
	@SerializedName("number")
	public String number;
	@SerializedName("practitioners")
	public List<Practitioner> practitioners = new ArrayList<>();
	@SerializedName("type")
	public String type;
}
