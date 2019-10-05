package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.data.R
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstantsHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) {
	private val constants: Constants = rawResourceHelper.getConstants(R.raw.constants)

	fun accountTypeLookUp(accountsString: String): String {
		return constants.account_type[accountsString] ?: accountsString
	}

	fun sicLookUp(sicString: String): String {
		return constants.sic_descriptions[sicString] ?: sicString
	}
}

@Singleton
class FilingHistoryDescriptionsHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) {
	private val filingHistoryDescriptions: FilingHistoryDescriptions = rawResourceHelper.getFilingHistoryDescriptions(R.raw.filing_history_descriptions)

	fun filingHistoryLookUp(filingHistoryDescriptionString: String): String {
		return filingHistoryDescriptions.description[filingHistoryDescriptionString] ?: filingHistoryDescriptionString
	}
}