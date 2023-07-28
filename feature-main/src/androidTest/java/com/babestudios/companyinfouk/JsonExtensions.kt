package com.babestudios.companyinfouk

import java.io.IOException
import java.io.InputStream

/**
 * This cannot be an extension on String as getClassLoader would fail in Java modules
 * @see [https://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Class.html#getClassLoader()]
 *
 * Added back temporarily, because [com.babestudios.base.kotlin.io.readResource] and
 * [com.babestudios.base.kotlin.io.readCommonResource] do not work.
 */
fun Any.loadJson(jsonFileName: String): String {
	val inputStream = this.javaClass.classLoader?.getResourceAsStream("$jsonFileName.json")
	return if (inputStream != null) readString(inputStream) else ""
}

@Throws(IOException::class)
fun readString(stream: InputStream): String {
	return String(stream.readBytes(), Charsets.UTF_8)
}
