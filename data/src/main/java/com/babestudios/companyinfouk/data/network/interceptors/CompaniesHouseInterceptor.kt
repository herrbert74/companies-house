package com.babestudios.companyinfouk.data.network.interceptors

import android.util.Base64
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CompaniesHouseInterceptor @Inject constructor(base64Wrapper: Base64Wrapper) : Interceptor {

	private val authorization: String = "Basic " + base64Wrapper.encodeToString(
			BuildConfig.COMPANIES_HOUSE_API_KEY.toByteArray(),
			Base64.NO_WRAP
	)
	override fun intercept(chain: Interceptor.Chain): Response {
		val requestBuilder = chain.request().newBuilder()
		var headersBuilder: Headers.Builder = chain.request().headers.newBuilder()
		headersBuilder = headersBuilder.add("Authorization", authorization)
		requestBuilder.headers(headersBuilder.build())
		val newRequest = requestBuilder.build()
		return chain.proceed(newRequest)
	}

}
