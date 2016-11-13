package com.babestudios.companieshouse.data.model.filinghistory;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DescriptionValues {
	@SerializedName("made_up_date")
	public String madeUpDate;

	@SerializedName("date")
	public String date;

	@SerializedName("capital")
	public List<Capital> capital = new ArrayList<>();

}
