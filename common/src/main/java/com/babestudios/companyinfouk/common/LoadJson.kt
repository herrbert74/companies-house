package com.babestudios.companyinfouk.common

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

fun Any.loadJson(jsonFileName: String):String {
	return try {
		val inputStream = this.javaClass.classLoader?.getResourceAsStream("$jsonFileName.json")
		String(toByteArray(inputStream), StandardCharsets.UTF_8)
	} catch (e: IOException) {
		e.printStackTrace()
		""
	}
}

private const val BYTE_ARRAY_SIZE = 4048

@Throws(IOException::class)
fun toByteArray(stream: InputStream?): ByteArray {
	val output = ByteArrayOutputStream()
	val bytes = ByteArray(BYTE_ARRAY_SIZE)
	var bytesRead = stream?.read(bytes) ?: 0
	while (bytesRead != -1) {
		output.write(bytes, 0, bytesRead)
		bytesRead = stream?.read(bytes) ?: 0
	}
	val result = output.toByteArray()
	output.close()
	return result
}
