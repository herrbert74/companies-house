package com.babestudios.companyinfouk.data.utils

import android.content.Context
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.babestudios.companyinfouk.data.local.apilookup.model.MortgageDescriptions
import com.babestudios.companyinfouk.data.local.apilookup.model.PscDescriptions
import com.google.gson.Gson

interface RawResourceHelperContract {
	fun getConstants(id: Int): Constants
	fun getFilingHistoryDescriptions(filingHistoryDescriptions: Int): FilingHistoryDescriptions
	fun getMortgageDescriptions(mortgageDescriptions: Int): MortgageDescriptions
	fun getPscDescriptions(pscDescriptions: Int): PscDescriptions
}

class RawResourceHelper constructor(val context: Context) : RawResourceHelperContract {
	override fun getConstants(id: Int): Constants {
		val constantsJsonString = context.resources.openRawResource(id).bufferedReader().use { it.readText() }
		return Gson().fromJson(constantsJsonString, Constants::class.java)
	}

	override fun getFilingHistoryDescriptions(filingHistoryDescriptions: Int): FilingHistoryDescriptions {
		val filingsJsonString = context
				.resources
				.openRawResource(filingHistoryDescriptions)
				.bufferedReader()
				.use { it.readText() }
		return Gson().fromJson(filingsJsonString, FilingHistoryDescriptions::class.java)
	}

	override fun getMortgageDescriptions(mortgageDescriptions: Int): MortgageDescriptions {
		val mortgageJsonString = context
				.resources
				.openRawResource(mortgageDescriptions)
				.bufferedReader()
				.use { it.readText() }
		return Gson().fromJson(mortgageJsonString, MortgageDescriptions::class.java)
	}

	override fun getPscDescriptions(pscDescriptions: Int): PscDescriptions {
		val pscJsonString = context
				.resources
				.openRawResource(pscDescriptions)
				.bufferedReader()
				.use { it.readText() }
		return Gson().fromJson(pscJsonString, PscDescriptions::class.java)
	}
}
