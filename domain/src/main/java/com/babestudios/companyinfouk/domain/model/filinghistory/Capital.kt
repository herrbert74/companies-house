package com.babestudios.companyinfouk.domain.model.filinghistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Capital(
		var figure: String = "",
		var currency: String = ""
): Parcelable
