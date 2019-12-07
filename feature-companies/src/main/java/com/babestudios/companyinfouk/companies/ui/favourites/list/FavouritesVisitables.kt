package com.babestudios.companyinfouk.companies.ui.favourites.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Suppress("UnnecessaryAbstractClass")
abstract class AbstractFavouritesVisitable {
	abstract fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int
}

@Parcelize
data class FavouritesVisitable(val favouritesListItem: FavouritesListItem)
	: AbstractFavouritesVisitable(), Parcelable, Comparable<FavouritesVisitable> {
	override fun compareTo(other: FavouritesVisitable): Int {
		return favouritesListItem
				.searchHistoryItem
				.companyNumber
				.compareTo(other
						.favouritesListItem
						.searchHistoryItem
						.companyNumber)
	}

	override fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int {
		return favouritesTypeFactory.type(favouritesListItem)
	}
}
