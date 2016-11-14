package com.babestudios.companieshouse.data.model.insolvency;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class InsolvencyCase {
	@SerializedName("dates")
	public List<Date> dates = new ArrayList<Date>();
	@SerializedName("links")
	public InsolvencyCaseLinks links;
	@SerializedName("notes")
	public List<String> notes = new ArrayList<String>();
	@SerializedName("number")
	public String number;
	@SerializedName("practitioners")
	public List<Practitioner> practitioners = new ArrayList<Practitioner>();
	@SerializedName("type")
	public String type;
}
