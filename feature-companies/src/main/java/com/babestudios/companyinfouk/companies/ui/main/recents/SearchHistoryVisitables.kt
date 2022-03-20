package com.babestudios.companyinfouk.companies.ui.main.recents

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import kotlinx.android.parcel.Parcelize

sealed class SearchHistoryVisitableBase : Parcelable {
	abstract fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int
}

@Parcelize
class SearchHistoryVisitable(val searchHistoryItem: SearchHistoryItem) : SearchHistoryVisitableBase(), Parcelable {
	override fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int {
		return searchHistoryTypeFactory.type(searchHistoryItem)
	}
}

@Parcelize
class SearchHistoryHeaderVisitable(val searchHistoryHeaderItem: SearchHistoryHeaderItem)
	: SearchHistoryVisitableBase(), Parcelable {
	override fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int {
		return searchHistoryTypeFactory.type(searchHistoryHeaderItem)
	}
}
