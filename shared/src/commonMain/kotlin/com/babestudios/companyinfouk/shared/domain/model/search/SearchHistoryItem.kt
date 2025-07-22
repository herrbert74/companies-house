package com.babestudios.companyinfouk.shared.domain.model.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchHistoryItem(
	var companyName: String,
	var companyNumber: String,
	private val searchTime: Long,
) {

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		val searchHistoryItem = other as SearchHistoryItem?
		return this.companyNumber == searchHistoryItem?.companyNumber
	}

	override fun hashCode(): Int {
		var result = companyName.hashCode()
		result = 31 * result + companyNumber.hashCode()
		result = 31 * result + searchTime.hashCode()
		return result
	}

}
