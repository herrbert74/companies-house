package com.babestudios.companyinfouk.data.utils.errors

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.data.utils.errors.model.ErrorBody
import com.google.gson.Gson

/**
 * Sometimes errorBody will contain only one ErrorEntity (e.g. with wrong auth header), but usually it's a full ErrorBody with ErrorEntities array
 *
 */
class CompaniesHouseErrorResolver(private val errorHelper: ErrorHelper) : ErrorResolver {
	override fun getErrorMessageFromResponseBody(errorJson: String?): String? {
		val errorBody = Gson().fromJson(errorJson, ErrorBody::class.java)
		val errorString = errorBody.error?.let { it }
				?: run { errorBody.errors?.get(0)?.error?.let { it2 -> it2 } }
				?: run { errorJson }

		return errorString?.let { errorHelper.errorLookUp(errorString) } ?: run { null }
	}

}