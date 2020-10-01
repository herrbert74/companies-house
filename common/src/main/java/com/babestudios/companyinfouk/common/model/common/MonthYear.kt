package com.babestudios.companyinfouk.common.model.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MonthYear(var year: Int = 0, var month: Int = 0) : Parcelable
