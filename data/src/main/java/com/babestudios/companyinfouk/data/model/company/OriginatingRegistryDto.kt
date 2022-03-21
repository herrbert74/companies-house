package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class OriginatingRegistryDto(

		@SerializedName("country")
		var country: String? = null,

		@SerializedName("name")
		var name: String? = null

) : Parcelable
