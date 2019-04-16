package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.local.apilookup.model.ErrorMappings
import com.google.gson.Gson

class ErrorHelper {
	private val errorMapping: ErrorMappings

	init {
		val text = CompaniesHouseApplication.context.resources.openRawResource(R.raw.errors).bufferedReader().use { it.readText() }
		errorMapping= Gson().fromJson<ErrorMappings>(text, ErrorMappings::class.java)
	}

	fun errorLookUp(errorString: String):String {
		return errorMapping.errors.service[errorString] ?: ""
	}
}