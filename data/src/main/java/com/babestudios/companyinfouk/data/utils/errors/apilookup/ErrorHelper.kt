package com.babestudios.companyinfouk.data.utils.errors.apilookup

import android.content.Context
import com.babestudios.companyinfouk.data.R
import com.babestudios.companyinfouk.data.utils.errors.apilookup.model.ErrorMappings
import com.google.gson.Gson

class ErrorHelper(private val context: Context) {
	private val errorMapping: ErrorMappings

	init {
		val text = context.resources.openRawResource(R.raw.errors).bufferedReader().use { it.readText() }
		errorMapping= Gson().fromJson<ErrorMappings>(text, ErrorMappings::class.java)
	}

	fun errorLookUp(errorString: String):String {
		return errorMapping.errors.service[errorString] ?: errorString
	}
}