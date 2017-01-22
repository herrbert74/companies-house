package com.babestudios.companyinfouk.data.model.persons;


import com.google.gson.annotations.SerializedName;

public class NameElements {
	@SerializedName("surname")
	public String surname;
	@SerializedName("title")
	public String title;
	@SerializedName("middle_name")
	public String middleName;
	@SerializedName("forename")
	public String forename;
}
