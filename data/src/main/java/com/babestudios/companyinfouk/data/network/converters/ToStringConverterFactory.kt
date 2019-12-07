package com.babestudios.companyinfouk.data.network.converters

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ToStringConverterFactory : Converter.Factory() {

	override fun responseBodyConverter(
			type: Type?,
			annotations: Array<Annotation>?,
			retrofit: Retrofit?
	): Converter<ResponseBody, String>? {
		if (String::class.java == type) {
			return Converter { value -> value.string() }
		}
		return null
	}

	override fun requestBodyConverter(
			type: Type?,
			parameterAnnotations: Array<Annotation>?,
			methodAnnotations: Array<Annotation>?,
			retrofit: Retrofit?
	): Converter<*, RequestBody>? {
		return if (String::class.java == type) {
			Converter<String, RequestBody> { value -> RequestBody.create(MEDIA_TYPE, value) }
		} else null
	}

	companion object {
		private val MEDIA_TYPE = "text/plain".toMediaTypeOrNull()

		fun create(): ToStringConverterFactory {
			return ToStringConverterFactory()
		}
	}
}
