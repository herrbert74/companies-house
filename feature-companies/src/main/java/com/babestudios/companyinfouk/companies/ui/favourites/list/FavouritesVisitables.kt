package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

abstract class AbstractFavouritesVisitable {
	abstract fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int
}

@Parcelize
data class FavouritesVisitable(val favouritesItem: FavouritesItem) : AbstractFavouritesVisitable(), Parcelable, Comparable<FavouritesVisitable> {
	override fun compareTo(other: FavouritesVisitable): Int {
		return favouritesItem.searchHistoryItem.companyNumber.compareTo(other.favouritesItem.searchHistoryItem.companyNumber)
	}

	override fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int {
		return favouritesTypeFactory.type(favouritesItem)
	}
}