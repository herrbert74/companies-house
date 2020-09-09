package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.data.R
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.babestudios.companyinfouk.data.local.apilookup.model.MortgageDescriptions
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import javax.inject.Inject
import javax.inject.Singleton

interface ConstantsHelperContract {
	fun accountTypeLookUp(accountsString: String): String
	fun sicLookUp(sicString: String): String
}

@Singleton
class ConstantsHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) : ConstantsHelperContract {
	private val constants: Constants = rawResourceHelper.getConstants(R.raw.constants)

	override fun accountTypeLookUp(accountsString: String): String {
		return constants.account_type[accountsString] ?: accountsString
	}

	override fun sicLookUp(sicString: String): String {
		return constants.sic_descriptions[sicString] ?: sicString
	}
}

@Singleton
class FilingHistoryDescriptionsHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) {
	private val filingHistoryDescriptions: FilingHistoryDescriptions = rawResourceHelper
			.getFilingHistoryDescriptions(R.raw.filing_history_descriptions)

	fun filingHistoryLookUp(filingHistoryDescriptionString: String): String {
		return filingHistoryDescriptions.description[filingHistoryDescriptionString] ?: filingHistoryDescriptionString
	}
}

@Singleton
class ChargesHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) {
	private val chargesDescriptions: MortgageDescriptions = rawResourceHelper
			.getMortgageDescriptions(R.raw.mortgage_descriptions)

	fun statusLookUp(statusString: String): String {
		return chargesDescriptions.status[statusString] ?: statusString
	}

	fun filingTypeLookUp(filingTypeString: String): String {
		return chargesDescriptions.filing_type[filingTypeString] ?: filingTypeString
	}
}
