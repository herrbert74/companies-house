package com.babestudios.companyinfouk.data.model.persons;

import com.google.gson.annotations.SerializedName;

public class Identification {
	@SerializedName("country_registered")
	public String countryRegistered;
	@SerializedName("legal_authority")
	public String legalAuthority;
	@SerializedName("legal_form")
	public String LegalForm;
	@SerializedName("place_registered")
	public String placeRegistered;
	@SerializedName("registration_number")
	public String registrationNumber;
}
