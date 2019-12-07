package com.babestudios.companyinfouk.companies.ui.main.recents

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Suppress("MatchingDeclarationName")//Intentional: Other item is in the model
data class SearchHistoryHeaderItem(val title: String) : Parcelable
