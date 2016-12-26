package com.babestudios.companieshouse.data.model.officers.appointments;


import com.babestudios.companieshouse.data.model.officers.Address;
import com.babestudios.companieshouse.data.model.officers.Identification;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Appointment {

	@SerializedName("address")
	public Address address;
	@SerializedName("appointed_before")
	public String appointedBefore;
	@SerializedName("appointed_on")
	public String appointedOn;
	@SerializedName("appointed_to")
	public AppointedTo appointedTo;
	@SerializedName("country_of_residence")
	public String countryOfResidence;
	@SerializedName("former_names")
	public List<FormerName> formerNames = null;
	@SerializedName("identification")
	public Identification identification;
	@SerializedName("is_pre_1992_appointment")
	public String isPre1992Appointment;
	@SerializedName("links")
	public Links links;
	@SerializedName("name")
	public String name;
	@SerializedName("name_elements")
	public NameElements nameElements;
	@SerializedName("nationality")
	public String nationality;
	@SerializedName("occupation")
	public String occupation;
	@SerializedName("officer_role")
	public String officerRole;
	@SerializedName("resigned_on")
	public String resignedOn;

}
