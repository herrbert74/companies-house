package com.babestudios.companyinfouk.common.model.filinghistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilingHistoryLinks(
		val documentMetadata: String = "",
		val self: String = ""
) : Parcelable
