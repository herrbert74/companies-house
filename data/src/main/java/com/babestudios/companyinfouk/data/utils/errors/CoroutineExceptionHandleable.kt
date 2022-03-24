package com.babestudios.companyinfouk.data.utils.errors

import kotlinx.coroutines.CoroutineExceptionHandler
import okhttp3.ResponseBody
import org.json.JSONObject

interface CoroutineExceptionHandleable {

	/**
	 * Use this when there is an HttpException and the error is sent inside the [okhttp3.ResponseBody], which can be
	 * retrieved from [retrofit2.Response.errorBody].
	 * @returns 1. Content of error field 2. Content of message field 3. A fallback string
	 */
	fun errorMessageFromResponseBody(responseBody: ResponseBody): String {
		val jsonObject = JSONObject(responseBody.string())
		val error = jsonObject.optString("error")
		return if (error.isNotEmpty()) error
		else jsonObject.optString("message", "An error happened, try again.")
	}

	/**
	 * Use this if the error is embedded in a 200 OK response and you want to trow an error.
	 * This can happen with e.g. GraphQL Apollo or whenever the back end dev thinks it's a good idea.
	 */
	fun errorMessageFromResponseObject(errorObject: Any?): String? = ""

	/**
	 * Implement this for each project to have a generic error resolver, that can be added to each network call
	 * through a compose operator.
	 * You could use onErrorReturn to transform a HttpException with [errorMessageFromResponseBody] (be careful not
	 * to throw the error(will only add the new one), but use Single.error<T> or similar), or a map operator to
	 * transform an embedded error with [errorMessageFromResponseObject].
	 *
	 * Add other reactive patterns (Observable, Flowable, etc. as needed)
	 */
	val exceptionHandler: CoroutineExceptionHandler

}
