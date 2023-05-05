package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ClassificationDto(
	@SerialName("description")
	var description: String? = null,
	@SerialName("type")
	var type: String? = null
) : Parcelable
