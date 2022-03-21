package com.babestudios.companyinfouk.data.model.insolvency


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class PractitionerDto(
		@SerializedName("address")
		var address: AddressDto? = null,
		@SerializedName("appointed_on")
		var appointedOn: String? = null,
		@SerializedName("ceased_to_act_on")
		var ceasedToActOn: String? = null,
		@SerializedName("name")
		var name: String? = null,
		@SerializedName("role")
		var role: String? = null
) : Parcelable
