package com.babestudios.companieshouse.data.model.officers;


import com.google.gson.annotations.SerializedName;

public class Identification {
	@SerializedName("place_registered")
	public String placeRegistered;
	@SerializedName("identification_type")
	public String identificationType;
	@SerializedName("registration_number")
	public String registrationNumber;
}
