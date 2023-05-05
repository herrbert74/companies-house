package com.babestudios.companyinfouk.data.model.common


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class SelfLinkDataDto(
		@SerialName("self")
		val self: String
) : Parcelable
