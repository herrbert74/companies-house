package com.babestudios.companyinfouk.shared.domain.model.common

import kotlinx.serialization.Serializable

@Serializable
data class Address(
	val addressLine1: String = "",
	val addressLine2: String? = null,
	val country: String? = null,
	val locality: String = "",
	val postalCode: String = "",
	val region: String? = null,
)

fun Address.getAddressString(): String {
	return this.addressLine2 ?: run {
		(this.addressLine1
			+ ", "
			+ this.locality
			+ ", "
			+ this.postalCode)
	}
}
