package com.babestudios.companyinfouk.data.model.insolvency


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class PractitionerDto(
		@SerialName("address")
		var address: AddressDto? = null,
		@SerialName("appointed_on")
		var appointedOn: String? = null,
		@SerialName("ceased_to_act_on")
		var ceasedToActOn: String? = null,
		@SerialName("name")
		var name: String? = null,
		@SerialName("role")
		var role: String? = null
) : Parcelable
