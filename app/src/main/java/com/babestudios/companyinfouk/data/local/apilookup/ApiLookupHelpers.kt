package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.google.gson.Gson

class ConstantsHelper {
	private val constants: Constants

	init {
		val constantsJsonString = CompaniesHouseApplication.context.resources.openRawResource(R.raw.constants).bufferedReader().use { it.readText() }
		constants= Gson().fromJson<Constants>(constantsJsonString, Constants::class.java)
	}

	fun accountTypeLookUp(accountsString: String):String {
		return constants.account_type[accountsString] ?: accountsString
	}

	fun sicLookUp(sicString: String):String {
		return constants.sic_descriptions[sicString] ?: sicString
	}
}

class FilingHistoryDescriptionsHelper {
	private val filingHistoryDescriptions: FilingHistoryDescriptions

	init {
		val filingHistoryDescriptionsJsonString = CompaniesHouseApplication.context.resources.openRawResource(R.raw.filing_history_descriptions).bufferedReader().use { it.readText() }
		filingHistoryDescriptions= Gson().fromJson<FilingHistoryDescriptions>(filingHistoryDescriptionsJsonString, FilingHistoryDescriptions::class.java)
	}

	fun filingHistoryLookUp(filingHistoryDescriptionString: String):String {
		return filingHistoryDescriptions.description[filingHistoryDescriptionString] ?: filingHistoryDescriptionString
	}
}