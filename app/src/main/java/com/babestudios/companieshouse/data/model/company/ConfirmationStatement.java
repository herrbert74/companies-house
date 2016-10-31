package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

public class ConfirmationStatement {

	@SerializedName("last_made_up_to")
	public String lastMadeUpTo;

	@SerializedName("next_due")
	public String nextDue;

	@SerializedName("next_made_up_to")
	public String nextMadeUpTo;

	@SerializedName("overdue")
	public String overdue;

}