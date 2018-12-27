package com.babestudios.base.ext

import okhttp3.ResponseBody
import org.json.JSONObject

fun ResponseBody.getErrorMessageFromResponseBody(): String {
	return try {
		val jsonObject = JSONObject(this.string())
		jsonObject.getString("error")
	} catch (e: Exception) {
		"An error happened, try again"
	}

}