package com.babestudios.companyinfouk.data.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SearchHistoryItem(
		var companyName: String,
		var companyNumber: String,
		private val searchTime: Long
) : Parcelable {

	override fun equals(other: Any?): Boolean {
		if (other == null) {
			return false
		}
		if (!SearchHistoryItem::class.java.isAssignableFrom(other.javaClass)) {
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
