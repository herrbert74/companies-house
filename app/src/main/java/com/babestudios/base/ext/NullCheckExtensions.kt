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

/**
 * When applied to a Pair of T and U, it returns the supplied lambda expression when T and U are not null, otherwise returns null
 */
fun <T, U, R> Pair<T?, U?>.biLet(body: (T, U) -> R): R? {
	val first = first
	val second = second
	if (first != null && second != null) {
		return body(first, second)
	}
	return null
}