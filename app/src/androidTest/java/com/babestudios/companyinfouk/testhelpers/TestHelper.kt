package com.babestudios.companyinfouk.testhelpers

import java.io.IOException
import java.io.InputStream


class TestHelper {
	fun loadJson(jsonFileName: String): String {
		val inputStream = this.javaClass.classLoader?.getResourceAsStream("$jsonFileName.json")
		inputStream?.let {
			return readString(inputStream)
		} ?: run { return "" }
	}

	@Throws(IOException::class)
	fun readString(stream: InputStream): String {
		return String(stream.readBytes(), Charsets.UTF_8)
	}
}