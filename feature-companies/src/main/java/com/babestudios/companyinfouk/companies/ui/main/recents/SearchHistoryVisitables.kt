package com.babestudios.companyinfouk.companies.ui.main.recents

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import kotlinx.android.parcel.Parcelize

abstract class AbstractSearchHistoryVisitable : Parcelable {
	abstract fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int
}

@Parcelize
class SearchHistoryVisitable(val searchHistoryItem: SearchHistoryItem) : AbstractSearchHistoryVisitable(), Parcelable {
	override fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int {
		return searchHistoryTypeFactory.type(searchHistoryItem)
	}
}

@Parcelize
class SearchHistoryHeaderVisitable(val searchHistoryHeaderItem: SearchHistoryHeaderItem)
	: AbstractSearchHistoryVisitable(), Parcelable {
	override fun type(searchHistoryTypeFactory: SearchHistoryAdapter.SearchHistoryTypeFactory): Int {
		return searchHistoryTypeFactory.type(searchHistoryHeaderItem)
	}
}
