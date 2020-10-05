package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.data.R
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.babestudios.companyinfouk.data.local.apilookup.model.MortgageDescriptions
import com.babestudios.companyinfouk.data.local.apilookup.model.PscDescriptions
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import javax.inject.Inject
import javax.inject.Singleton

interface ConstantsHelperContract {
	fun accountTypeLookUp(accountsString: String): String
	fun sicLookUp(sicString: String): String
	fun insolvencyCaseType(caseTypeKey: String): String
	fun insolvencyCaseDateType(caseDateTypeKey: String): String
	fun officerRoleLookup(officerRoleKey: String): String
}

interface FilingHistoryDescriptionsHelperContract {
	fun filingHistoryLookUp(filingHistoryDescriptionString: String): String
}

interface ChargesHelperContract {
	fun statusLookUp(statusString: String): String
	fun filingTypeLookUp(filingTypeString: String): String
}

interface PscHelperContract {
	fun shortDescriptionLookUp(statusString: String): String
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

	override fun insolvencyCaseType(caseTypeKey: String): String {
		return constants.insolvency_case_type[caseTypeKey] ?: caseTypeKey
	}

	override fun insolvencyCaseDateType(caseDateTypeKey: String): String {
		return constants.insolvency_case_date_type[caseDateTypeKey] ?: caseDateTypeKey
	}

	override fun officerRoleLookup(officerRoleKey: String): String {
		return constants.officer_role[officerRoleKey] ?: officerRoleKey
	}
}

@Singleton
class FilingHistoryDescriptionsHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract)
	: FilingHistoryDescriptionsHelperContract {
	private val filingHistoryDescriptions: FilingHistoryDescriptions = rawResourceHelper
			.getFilingHistoryDescriptions(R.raw.filing_history_descriptions)

	override fun filingHistoryLookUp(filingHistoryDescriptionString: String): String {
		return filingHistoryDescriptions.description[filingHistoryDescriptionString] ?: filingHistoryDescriptionString
	}
}

@Singleton
class ChargesHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) : ChargesHelperContract {
	private val chargesDescriptions: MortgageDescriptions = rawResourceHelper
			.getMortgageDescriptions(R.raw.mortgage_descriptions)

	override fun statusLookUp(statusString: String): String {
		return chargesDescriptions.status[statusString] ?: statusString
	}

	override fun filingTypeLookUp(filingTypeString: String): String {
		return chargesDescriptions.filing_type[filingTypeString] ?: filingTypeString
	}
}

@Singleton
class PscHelper @Inject constructor(rawResourceHelper: RawResourceHelperContract) : PscHelperContract {
	private val pscDescriptions: PscDescriptions = rawResourceHelper
			.getPscDescriptions(R.raw.psc_descriptions)

	override fun shortDescriptionLookUp(shortdescriptionString: String): String {
		return pscDescriptions.short_description[shortdescriptionString] ?: shortdescriptionString
	}
}
