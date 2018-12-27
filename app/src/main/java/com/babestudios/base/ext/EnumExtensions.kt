package com.babestudios.base.ext

import com.google.gson.annotations.SerializedName

fun Enum<*>.getSerializedName(): String {
	return try {
		val f = this.javaClass.getField(this.name)
		val a = f.getAnnotation(SerializedName::class.java)
		a?.value ?: ""
	} catch (ignored: NoSuchFieldException) {
		""
	}

}