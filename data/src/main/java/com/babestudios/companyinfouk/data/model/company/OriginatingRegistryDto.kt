package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class OriginatingRegistryDto(

		@SerialName("country")
		var country: String? = null,

		@SerialName("name")
		var name: String? = null

) : Parcelable
