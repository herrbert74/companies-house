package com.babestudios.companyinfouk.common.model.filinghistory

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Capital(
		var figure: String = "",
		var currency: String = ""
): Parcelable
