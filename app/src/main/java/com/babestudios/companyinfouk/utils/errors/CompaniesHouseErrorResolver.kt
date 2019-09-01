package com.babestudios.companyinfouk.utils.errors

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.utils.errors.model.ErrorBody
import com.google.gson.Gson

/**
 * TODO Move this to the Companies House app along with the Model and the lookup classes. Dagger limitations prevent this currently.
 *
 * Sometimes errorBody will contain only one ErrorEntity (e.g. with wrong auth header), but usually it's a full ErrorBody with ErrorEntities array
 *
 */
class CompaniesHouseErrorResolver : ErrorResolver {
	override fun getErrorMessageFromResponseBody(errorJson: String?): String? {
		val errorBody = Gson().fromJson(errorJson, ErrorBody::class.java)
		val errorString = errorBody.error?.let { it }
				?: run { errorBody.errors?.get(0)?.error?.let { it2 -> it2 } }
				?: run { errorJson }

		return errorString?.let { ErrorHelper().errorLookUp(errorString) } ?: run { null }
	}

}