package com.babestudios.companyinfouk.domain.model.filinghistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Capital(
		var figure: String = "",
		var currency: String = ""
): Parcelable
