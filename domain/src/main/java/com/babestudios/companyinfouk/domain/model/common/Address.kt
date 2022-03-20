package com.babestudios.companyinfouk.domain.model.common

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

fun Address.getAddressString(): String {
	return this.addressLine2 ?: run {
		(this.addressLine1
				+ ", "
				+ this.locality
				+ ", "
				+ this.postalCode)
	}
}
