package com.babestudios.companyinfouk.shared.data.model.filinghistory

import kotlinx.serialization.SerialName

enum class CategoryDto {

	@SerialName("")
	CATEGORY_SHOW_ALL,

	@SerialName("gazette")
	CATEGORY_GAZETTE,

	@SerialName("confirmation-statement")
	CATEGORY_CONFIRMATION_STATEMENT,

	@SerialName("accounts")
	CATEGORY_ACCOUNTS,

	@SerialName("annual-return")
	CATEGORY_ANNUAL_RETURN,

	@SerialName("officers")
	CATEGORY_OFFICERS,

	@SerialName("address")
	CATEGORY_ADDRESS,

	@SerialName("capital")
	CATEGORY_CAPITAL,

	@SerialName("insolvency")
	CATEGORY_INSOLVENCY,

	@SerialName("other")
	CATEGORY_OTHER,

	@SerialName("incorporation")
	CATEGORY_INCORPORATION,

	@SerialName("change-of-constitution")
	CATEGORY_CONSTITUTION,

	@SerialName("change-of-name")
	CATEGORY_NAME,

	@SerialName("auditors")
	CATEGORY_AUDITORS,

	@SerialName("resolution")
	CATEGORY_RESOLUTION,

	@SerialName("mortgage")
	CATEGORY_MORTGAGE,

	@SerialName("persons-with-significant-control")
	CATEGORY_PERSONS,

	@SerialName("miscellaneous")
	CATEGORY_MISCELLANEOUS,

	@SerialName("document-replacement")
	CATEGORY_DOCUMENT_REPLACEMENT;

}
