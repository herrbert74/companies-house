package com.babestudios.companyinfo.data.utils

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.companyinfo.data.local.apilookup.model.Constants
import com.babestudios.companyinfo.data.local.apilookup.model.FilingHistoryDescriptions
import com.google.gson.Gson

interface RawResourceHelperContract {
	fun getConstants(id: Int): Constants
	fun getFilingHistoryDescriptions(filing_history_descriptions: Int): FilingHistoryDescriptions
}

class RawResourceHelper(@ApplicationContext val context: Context) : RawResourceHelperContract {
	override fun getConstants(id: Int): Constants {
		val constantsJsonString = context.resources.openRawResource(id).bufferedReader().use { it.readText() }
		return Gson().fromJson<Constants>(constantsJsonString, Constants::class.java)
	}

	override fun getFilingHistoryDescriptions(filing_history_descriptions: Int): FilingHistoryDescriptions {
		val constantsJsonString = context.resources.openRawResource(filing_history_descriptions).bufferedReader().use { it.readText() }
		return Gson().fromJson<FilingHistoryDescriptions>(constantsJsonString, FilingHistoryDescriptions::class.java)
	}
}