package com.babestudios.companyinfouk.shared.domain.model.filinghistory

/**
 * FilingHistory Category as used in the presentation layer.
 * @param displayName The name that is displayed in the presentation layer.
 * @param serialName The name that is used by the Companies House API. This was added here to enable filtering
 * through the API, without the need to use reflection to get the serialized name, as it was before.
 *
 * The documentation on this is outdated as of 2022:
 * https://developer-specs.company-information.service.gov.uk/companies-house-public-data-api/resources/filinghistoryitem?v=latest
 *
 * This confirms that Confirmation Statements replaced Annual Returns and the rest is just missing from the docs.
 * https://forum.aws.chdev.org/t/filings-history-category-filter/4971
 *
 * This was filed for this issue, but could be closed:
 * https://bitbucket.org/herrbert74/companies-house/issues/77/filinghistory-problems
 */
enum class Category(val displayName: String, val serialName: String) {
	CATEGORY_SHOW_ALL("all", ""),
	CATEGORY_GAZETTE("gazette", "gazette"),
	CATEGORY_CONFIRMATION_STATEMENT("confirmation statement", "confirmation-statement"),
	CATEGORY_ACCOUNTS("accounts", "accounts"),
	CATEGORY_ANNUAL_RETURN("annual return", "annual-return"),
	CATEGORY_OFFICERS("officers", "officers"),
	CATEGORY_ADDRESS("address", "address"),
	CATEGORY_CAPITAL("capital", "capital"),
	CATEGORY_INSOLVENCY("insolvency", "insolvency"),
	CATEGORY_OTHER("other", "other"),
	CATEGORY_INCORPORATION("incorporation", "incorporation"),
	CATEGORY_CONSTITUTION("change of constitution", "change-of-constitution"),
	CATEGORY_AUDITORS("auditors", "auditors"),
	CATEGORY_RESOLUTION("resolution", "resolution"),
	CATEGORY_MORTGAGE("mortgage", "mortgage"),
	CATEGORY_PERSONS("persons with significant control", "persons-with-significant-control");

	override fun toString(): String {
		return displayName
	}
}
