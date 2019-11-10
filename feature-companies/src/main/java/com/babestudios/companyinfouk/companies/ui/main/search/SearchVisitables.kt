package com.babestudios.companyinfouk.companies.ui.main.search

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem
import kotlinx.android.parcel.Parcelize

abstract class AbstractSearchVisitable : Parcelable {
	abstract fun type(searchTypeFactory: SearchAdapter.SearchTypeFactory): Int
}

@Parcelize
class SearchVisitable(val searchItem: CompanySearchResultItem) : AbstractSearchVisitable(), Parcelable {
	override fun type(searchTypeFactory: SearchAdapter.SearchTypeFactory): Int {
		return searchTypeFactory.type(searchItem)
	}
}