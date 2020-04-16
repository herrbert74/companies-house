package com.babestudios.companyinfouk.common.model.filinghistory

import androidx.annotation.Keep

@Keep
enum class Category(val displayName: String) {
	CATEGORY_SHOW_ALL("all"),
	CATEGORY_GAZETTE("gazette"),
	CATEGORY_CONFIRMATION_STATEMENT("confirmation statement"),
	CATEGORY_ACCOUNTS("accounts"),
	CATEGORY_ANNUAL_RETURN("annual return"),
	CATEGORY_OFFICERS("officers"),
	CATEGORY_ADDRESS("address"),
	CATEGORY_CAPITAL("capital"),
	CATEGORY_INSOLVENCY("insolvency"),
	CATEGORY_OTHER("other"),
	CATEGORY_INCORPORATION("incorporation"),
	CATEGORY_CONSTITUTION("change of constitution"),
	CATEGORY_AUDITORS("auditors"),
	CATEGORY_RESOLUTION("resolution"),
	CATEGORY_MORTGAGE("mortgage");

	override fun toString(): String {
		return displayName
	}
}
