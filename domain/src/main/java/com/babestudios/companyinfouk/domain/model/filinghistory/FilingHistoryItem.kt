package com.babestudios.companyinfouk.domain.model.filinghistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilingHistoryItem(
	val date: String = "",
	val type: String = "",
	val links: FilingHistoryLinks = FilingHistoryLinks(),
	val category: Category = Category.CATEGORY_SHOW_ALL,
	val subcategory: String = "",
	val description: String = "",
	val pages: Int = 0,
):Parcelable
