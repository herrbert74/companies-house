package com.babestudios.companyinfouk.data.model.filinghistory

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
enum class CategoryDto(val displayName: String) {
	@SerializedName("")
	CATEGORY_SHOW_ALL("all"),
	@SerializedName("gazette")
	CATEGORY_GAZETTE("gazette"),
	@SerializedName("confirmation-statement")
	CATEGORY_CONFIRMATION_STATEMENT("confirmation statement"),
	@SerializedName("accounts")
	CATEGORY_ACCOUNTS("accounts"),
	@SerializedName("annual-return")
	CATEGORY_ANNUAL_RETURN("annual return"),
	@SerializedName("officers")
	CATEGORY_OFFICERS("officers"),
	@SerializedName("address")
	CATEGORY_ADDRESS("address"),
	@SerializedName("capital")
	CATEGORY_CAPITAL("capital"),
	@SerializedName("insolvency")
	CATEGORY_INSOLVENCY("insolvency"),
	@SerializedName("other")
	CATEGORY_OTHER("other"),
	@SerializedName("incorporation")
	CATEGORY_INCORPORATION("incorporation"),
	@SerializedName("change-of-constitution")
	CATEGORY_CONSTITUTION("change of constitution"),
	@SerializedName("auditors")
	CATEGORY_AUDITORS("auditors"),
	@SerializedName("resolution")
	CATEGORY_RESOLUTION("resolution"),
	@SerializedName("mortgage")
	CATEGORY_MORTGAGE("mortgage");

	override fun toString(): String {
		return displayName
	}
}
