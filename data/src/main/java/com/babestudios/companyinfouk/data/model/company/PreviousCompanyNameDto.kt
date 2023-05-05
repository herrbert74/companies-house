package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class PreviousCompanyNameDto(

		@SerialName("ceased_on")
		var ceasedOn: String? = null,

		@SerialName("effective_from")
		var effectiveFrom: String? = null,

		@SerialName("name")
		var name: String? = null

) : Parcelable
