package com.babestudios.base.ext

fun <T: Any, R: Any> Collection<T?>.whenAllNotNull(block: (List<T>)->R) {
	if (this.all { it != null }) {
		block(this.filterNotNull()) // or do unsafe cast to non null collection
	}
}

fun <T: Any, R: Any> Collection<T?>.whenAnyNotNull(block: (List<T>)->R) {
	if (this.any { it != null }) {
		block(this.filterNotNull())
	}
}