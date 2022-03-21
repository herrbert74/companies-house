package com.babestudios.companyinfouk.domain.model.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MonthYear(val year: Int?, val month: Int?) : Parcelable
