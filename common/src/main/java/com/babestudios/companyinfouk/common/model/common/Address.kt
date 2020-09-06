package com.babestudios.companyinfouk.common.model.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Address(
		val addressLine1: String = "",
		val addressLine2: String = "",
		val country: String = "",
		val locality: String = "",
		val postalCode: String = "",
		val region: String = ""
) : Parcelable
