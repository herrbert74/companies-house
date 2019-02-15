package com.babestudios.companyinfouk.ui.favourites

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.favourites.list.FavouritesVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	FAVOURITES_RECEIVED
}

@Parcelize
data class FavouritesState(
		var favouriteItems: List<FavouritesVisitable>?,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable