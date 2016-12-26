package com.babestudios.companieshouse.data.model.officers.appointments;


import com.babestudios.companieshouse.data.model.officers.DateOfBirth;
import com.babestudios.companieshouse.data.model.officers.OfficersLinks;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Appointments {
	@SerializedName("date_of_birth")
	public DateOfBirth dateOfBirth;
	@SerializedName("etag")
	public String etag;
	@SerializedName("is_corporate_officer")
	public String isCorporateOfficer;
	@SerializedName("items")
	public List<Appointment> items = null;
	@SerializedName("items_per_page")
	public String itemsPerPage;
	@SerializedName("kind")
	public String kind;
	@SerializedName("links")
	public OfficersLinks links;
	@SerializedName("name")
	public String name;
	@SerializedName("start_index")
	public String startIndex;
	@SerializedName("total_results")
	public String totalResults;
}
