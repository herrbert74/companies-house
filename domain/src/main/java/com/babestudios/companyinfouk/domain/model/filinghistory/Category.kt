package com.babestudios.companyinfouk.domain.model.filinghistory

import androidx.annotation.Keep

@Keep
/**
 * The documentation on this is outdated as of 2022:
 * https://developer-specs.company-information.service.gov.uk/companies-house-public-data-api/resources/filinghistoryitem?v=latest
 *
 * This confirms that Confirmation Statements replaced Annual Returns and the rest is just missing from the docs.
 * https://forum.aws.chdev.org/t/filings-history-category-filter/4971
 *
 * This was filed for this issue, but could be closed:
 * https://bitbucket.org/herrbert74/companies-house/issues/77/filinghistory-problems
 */
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
	CATEGORY_MORTGAGE("mortgage"),
	CATEGORY_PERSONS("persons with significant control");

	override fun toString(): String {
		return displayName
	}
}
