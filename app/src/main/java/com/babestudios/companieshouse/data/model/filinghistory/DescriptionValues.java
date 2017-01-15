package com.babestudios.companieshouse.data.model.filinghistory;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DescriptionValues {
	@SerializedName("made_up_date")
	public String madeUpDate;

	@SerializedName("officer_name")
	public String officerName;

	@SerializedName("appointment_date")
	public String appointmentDate;

	@SerializedName("termination_date")
	public String terminationDate;

	@SerializedName("new_date")
	public String newDate;

	@SerializedName("change_date")
	public String changeDate;

	@SerializedName("date")
	public String date;

	@SerializedName("capital")
	public List<Capital> capital = new ArrayList<>();

}
