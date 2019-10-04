package com.babestudios.companyinfouk.ui.favourites.list

import android.os.Parcelable
import com.babestudios.companyinfo.data.model.search.SearchHistoryItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavouritesItem(val searchHistoryItem: SearchHistoryItem, var isPendingRemoval: Boolean = false) : Parcelable