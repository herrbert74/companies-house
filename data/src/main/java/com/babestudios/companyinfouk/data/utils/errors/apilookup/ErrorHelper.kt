package com.babestudios.companyinfouk.data.utils.errors.apilookup

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.companyinfouk.data.R
import com.babestudios.companyinfouk.data.utils.errors.apilookup.model.ErrorMappings
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHelper @Inject constructor(@ApplicationContext context: Context) {
	private val errorMapping: ErrorMappings

	init {
		val text = context.resources.openRawResource(R.raw.errors).bufferedReader().use { it.readText() }
		errorMapping = Gson().fromJson<ErrorMappings>(text, ErrorMappings::class.java)
	}

	fun errorLookUp(errorString: String): String {
		return errorMapping.errors.service[errorString] ?: errorString
	}
}