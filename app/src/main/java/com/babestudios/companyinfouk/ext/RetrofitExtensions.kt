package com.babestudios.companyinfouk.ext

import com.babestudios.companyinfouk.data.local.apilookup.model.ErrorMappings
import com.babestudios.companyinfouk.data.model.ErrorBody
import com.google.gson.Gson
import okhttp3.ResponseBody
import org.json.JSONObject

fun ResponseBody.getErrorMessageFromResponseBody(): String {
	return try {
		val body = Gson().fromJson(this.string(), ErrorBody::class.java)
		return body.errors[0].error
		/*val jsonObject = JSONObject(this.string())
		jsonObject.getString("error")*/
	} catch (e: Exception) {
		"An error happened, try again"
	}

}