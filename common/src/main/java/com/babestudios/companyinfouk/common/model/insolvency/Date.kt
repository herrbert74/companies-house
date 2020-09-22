package com.babestudios.companyinfouk.common.model.insolvency


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Date (
	var date: String? = null,
	var type: String? = null
): Parcelable
