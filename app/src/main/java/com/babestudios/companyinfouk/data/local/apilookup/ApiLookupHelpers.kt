package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.local.apilookup.model.Error
import com.google.gson.Gson

class ErrorHelper {
	init {
		val text = CompaniesHouseApplication.context.resources.openRawResource(R.raw.errors).bufferedReader().use { it.readText() }
		val errorMapping: Error = Gson().fromJson<Error>(text, Error::class.java)
	}
}