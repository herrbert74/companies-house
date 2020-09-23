package com.babestudios.companyinfouk.common.model.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Address(
		val addressLine1: String = "",
		val addressLine2: String? = null,
		val country: String? = null,
		val locality: String = "",
		val postalCode: String = "",
		val region: String? = null,
) : Parcelable
