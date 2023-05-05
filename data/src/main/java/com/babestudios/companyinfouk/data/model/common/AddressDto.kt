package com.babestudios.companyinfouk.data.model.common


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AddressDto(
		@SerialName("address_line_1")
		var addressLine1: String? = null,
		@SerialName("address_line_2")
		var addressLine2: String? = null,
		@SerialName("country")
		var country: String? = null,
		@SerialName("locality")
		var locality: String? = null,
		@SerialName("postal_code")
		var postalCode: String? = null,
		@SerialName("region")
		var region: String? = null,
		@SerialName("premises")
		var premises: String? = null,
		@SerialName("care_of")
		var careOf: String? = null,
		@SerialName("po_box")
		var poBox: String? = null
) : Parcelable
