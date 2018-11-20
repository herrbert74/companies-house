package com.babestudios.base.ext

import java.text.DateFormat
import java.util.*

fun Long.toDateString(dateFormat: Int = DateFormat.SHORT): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}
fun Long.toTimeString(dateFormat: Int = DateFormat.SHORT): String {
	val df = DateFormat.getTimeInstance(dateFormat, Locale.getDefault())
	return df.format(this)
}