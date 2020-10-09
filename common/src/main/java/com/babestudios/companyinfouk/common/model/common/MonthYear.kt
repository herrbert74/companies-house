package com.babestudios.companyinfouk.common.model.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonthYear(val year: Int?, val month: Int?) : Parcelable
