
package com.babestudios.companyinfouk.data.model.company;

import com.google.gson.annotations.SerializedName;

public class AnnualReturn {

	@SerializedName("last_made_up_to")
	public String lastMadeUpTo;

	@SerializedName("next_due")
	public String nextDue;

	public boolean overdue;

	@SerializedName("next_made_up_to")
	public String nextMadeUpTo;

}
