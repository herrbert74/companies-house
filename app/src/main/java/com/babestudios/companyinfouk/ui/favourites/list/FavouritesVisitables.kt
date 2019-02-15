package com.babestudios.companyinfouk.ui.favourites.list

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

abstract class AbstractFavouritesVisitable {
	abstract fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int
}

@Parcelize
class FavouritesVisitable(val favouritesItem: FavouritesItem) : AbstractFavouritesVisitable(), Parcelable {
	override fun type(favouritesTypeFactory: FavouritesAdapter.FavouritesTypeFactory): Int {
		return favouritesTypeFactory.type(favouritesItem)
	}
}