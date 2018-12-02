package com.babestudios.companyinfouk.data.model.company

import com.google.gson.annotations.SerializedName

class ConfirmationStatement {

	@SerializedName("last_made_up_to")
	var lastMadeUpTo: String? = null

	@SerializedName("next_due")
	var nextDue: String? = null

	@SerializedName("next_made_up_to")
	var nextMadeUpTo: String? = null

	@SerializedName("overdue")
	var overdue: String? = null

}