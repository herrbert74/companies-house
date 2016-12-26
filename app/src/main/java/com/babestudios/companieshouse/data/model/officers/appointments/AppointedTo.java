package com.babestudios.companieshouse.data.model.officers.appointments;


import com.google.gson.annotations.SerializedName;

public class AppointedTo {

	@SerializedName("company_name")
	public String companyName;
	@SerializedName("company_number")
	public String companyNumber;
	@SerializedName("company_status")
	public String companyStatus;

}