package com.babestudios.companyinfouk.data.utils.errors

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.data.utils.errors.model.ErrorBody
import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sometimes errorBody will contain only one ErrorEntity (e.g. with wrong auth header),
 * but usually it's a full ErrorBody with ErrorEntities array
 *
 */
@Singleton
class CompaniesHouseErrorResolver @Inject constructor(private val errorHelper: ErrorHelper) : ErrorResolver {
	override fun getErrorMessageFromResponseBody(errorJson: String?): String? {
		val errorString = try {
			val errorBody = Gson().fromJson(errorJson, ErrorBody::class.java)
			errorBody.error
					?: run { errorBody.errors?.get(0)?.error }
					?: run { errorJson }
		} catch (e: JsonParseException) {
			when {
				errorJson?.contains("HTTP 401") == true
				-> "Authentication error. Please try again"
				else -> "An error happened. Please try again"
			}
		} catch (e: IllegalStateException) {
			"An error happened. Please try again"
		}
		return errorString?.let { errorHelper.errorLookUp(errorString) } ?: run { null }
	}
}
