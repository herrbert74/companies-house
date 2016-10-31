package com.babestudios.companieshouse.data.model.company;

import com.google.gson.annotations.SerializedName;

public class Accounts {

	@SerializedName("accounting_reference_date")
	public AccountingReferenceDate accountingReferenceDate;

	@SerializedName("last_accounts")
	public LastAccounts lastAccounts;

	@SerializedName("next_due")
	public String nextDue;

	public boolean overdue;

	@SerializedName("next_made_up_to")
	public String nextMadeUpTo;
}
