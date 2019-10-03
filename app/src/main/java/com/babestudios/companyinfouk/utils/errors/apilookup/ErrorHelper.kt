package com.babestudios.companyinfouk.utils.errors.apilookup

import com.babestudios.base.BaseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.utils.errors.apilookup.model.ErrorMappings
import com.google.gson.Gson

class ErrorHelper {
	private val errorMapping: ErrorMappings

	init {
		val text = BaseApplication.context.resources.openRawResource(R.raw.errors).bufferedReader().use { it.readText() }
		errorMapping= Gson().fromJson<ErrorMappings>(text, ErrorMappings::class.java)
	}

	fun errorLookUp(errorString: String):String {
		return errorMapping.errors.service[errorString] ?: errorString
	}
}