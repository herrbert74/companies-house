package com.babestudios.companyinfouk.domain.model.filinghistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilingHistoryLinks(
		val documentMetadata: String = "",
		val self: String = "",
) : Parcelable
