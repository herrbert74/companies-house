package com.babestudios.companyinfouk.data.utils.errors

import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.data.utils.errors.model.ErrorBody
import io.reactivex.Single
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sometimes errorBody will contain only one ErrorEntity (e.g. with wrong auth header),
 * but usually it's a full ErrorBody with ErrorEntities array
 *
 */
@Singleton
class CompaniesHouseRxErrorResolver @Inject constructor(
		@Suppress("unused") private val errorHelper: ErrorHelper
) : ErrorResolver {

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
	 * TODO What if it sends errors instead of error (could not find an example so far)?
	 */
	@Suppress("RemoveExplicitTypeArguments")
	override fun <T> resolveErrorForSingle(): (Single<T>) -> Single<T> {
		return { single ->
			single.onErrorResumeNext {
				(it as? HttpException)?.response()?.errorBody()?.let { body ->
					Single.error<T> { Exception(errorMessageFromResponseBody(body)) }
				} ?: Single.error<T> { Exception("An error happened") }
			}
		}
	}
}
