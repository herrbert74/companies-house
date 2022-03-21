@file:Suppress("MatchingDeclarationName")
package com.babestudios.companyinfouk.companies.ui.main.recents

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchHistoryHeaderItem(val title: String) : Parcelable
