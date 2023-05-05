package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class NameElementsDto(
		@SerialName("surname")
		var surname: String? = null,
		@SerialName("title")
		var title: String? = null,
		@SerialName("middle_name")
		var middleName: String? = null,
		@SerialName("forename")
		var forename: String? = null
) : Parcelable
