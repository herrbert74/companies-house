package com.babestudios.companyinfouk.shared.data.model.filinghistory

import kotlinx.serialization.SerialName

enum class CategoryDto(val displayName: String) {
	@SerialName("")
	CATEGORY_SHOW_ALL("all"),
	@SerialName("gazette")
	CATEGORY_GAZETTE("gazette"),
	@SerialName("confirmation-statement")
	CATEGORY_CONFIRMATION_STATEMENT("confirmation statement"),
	@SerialName("accounts")
	CATEGORY_ACCOUNTS("accounts"),
	@SerialName("annual-return")
	CATEGORY_ANNUAL_RETURN("annual return"),
	@SerialName("officers")
	CATEGORY_OFFICERS("officers"),
	@SerialName("address")
	CATEGORY_ADDRESS("address"),
	@SerialName("capital")
	CATEGORY_CAPITAL("capital"),
	@SerialName("insolvency")
	CATEGORY_INSOLVENCY("insolvency"),
	@SerialName("other")
	CATEGORY_OTHER("other"),
	@SerialName("incorporation")
	CATEGORY_INCORPORATION("incorporation"),
	@SerialName("change-of-constitution")
	CATEGORY_CONSTITUTION("change of constitution"),
	@SerialName("auditors")
	CATEGORY_AUDITORS("auditors"),
	@SerialName("resolution")
	CATEGORY_RESOLUTION("resolution"),
	@SerialName("mortgage")
	CATEGORY_MORTGAGE("mortgage"),
	@SerialName("persons-with-significant-control")
	CATEGORY_PERSONS("persons with significant control");

	override fun toString(): String {
		return displayName
	}
}
