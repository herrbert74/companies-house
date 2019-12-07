package com.babestudios.companyinfouk.data.utils

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.companyinfouk.data.local.apilookup.model.Constants
import com.babestudios.companyinfouk.data.local.apilookup.model.FilingHistoryDescriptions
import com.google.gson.Gson
import javax.inject.Inject

interface RawResourceHelperContract {
	fun getConstants(id: Int): Constants
	fun getFilingHistoryDescriptions(filingHistoryDescriptions: Int): FilingHistoryDescriptions
}

class RawResourceHelper @Inject constructor(@ApplicationContext val context: Context) : RawResourceHelperContract {
	override fun getConstants(id: Int): Constants {
		val constantsJsonString = context.resources.openRawResource(id).bufferedReader().use { it.readText() }
		return Gson().fromJson<Constants>(constantsJsonString, Constants::class.java)
	}

	override fun getFilingHistoryDescriptions(filingHistoryDescriptions: Int): FilingHistoryDescriptions {
		val constantsJsonString = context
				.resources
				.openRawResource(filingHistoryDescriptions)
				.bufferedReader()
				.use { it.readText() }
		return Gson().fromJson<FilingHistoryDescriptions>(constantsJsonString, FilingHistoryDescriptions::class.java)
	}
}
