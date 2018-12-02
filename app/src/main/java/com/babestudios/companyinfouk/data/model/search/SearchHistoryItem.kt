package com.babestudios.companyinfouk.data.model.search


import java.util.Objects

class SearchHistoryItem(var companyName: String, var companyNumber: String, private val searchTime: Long) {

	override fun equals(searchItem: Any?): Boolean {
		if (searchItem == null) {
			return false
		}
		if (!SearchHistoryItem::class.java.isAssignableFrom(searchItem.javaClass)) {
			return false
		}
		val other = searchItem as SearchHistoryItem?
		return this.companyNumber == other!!.companyNumber
	}

}
