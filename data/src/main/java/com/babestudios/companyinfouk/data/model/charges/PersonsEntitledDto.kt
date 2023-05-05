package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PersonsEntitledDto(
		@SerialName("name")
		var name: String? = null
) : Parcelable
