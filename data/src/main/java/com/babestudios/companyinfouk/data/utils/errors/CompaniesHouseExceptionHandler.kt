package com.babestudios.companyinfouk.data.utils.errors

import com.babestudios.base.data.network.CoroutineExceptionHandleable
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.data.utils.errors.model.ErrorBody
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 *
 * Not used as CH API does not send handleable exceptions. Some parts were commented out during
 * the switch from Retrofit to Ktor.
 *
 * Sometimes errorBody will contain only one ErrorEntity (e.g. with wrong auth header),
 * but usually it's a full ErrorBody with ErrorEntities array
 *
 */
internal class CompaniesHouseExceptionHandler constructor(
	@Suppress("unused") private val errorHelper: ErrorHelper
) : CoroutineExceptionHandleable {

	/**
	 * It seems this is not needed. There are errors in CH API, but they are not used anymore(?!)
	 * E.g. empty error is not sent, but empty collection instead.
	 * Hence [ErrorHelper] is not used at the moment.
	 */
	override fun errorMessageFromResponseObject(errorObject: Any?): String? {
		return null
	}

	/**
	 * [errorMessageFromResponseBody] could be expanded by using [ErrorBody],
	 * but only [ErrorBody.error] is used, so the default is sufficient for now.
	 */
	@Suppress("TooGenericExceptionThrown")
	override val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
		when (throwable) {
//			is HttpException -> throwable.response()?.errorBody()?.let { body ->
//				throw Exception(errorMessageFromResponseBody(body))
//			}
			else -> throw Exception("An error happened")
		}
	}
}
