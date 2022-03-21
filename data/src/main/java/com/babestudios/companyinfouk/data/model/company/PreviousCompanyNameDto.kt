package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PreviousCompanyNameDto(

		@SerializedName("ceased_on")
		var ceasedOn: String? = null,

		@SerializedName("effective_from")
		var effectiveFrom: String? = null,

		@SerializedName("name")
		var name: String? = null

) : Parcelable
