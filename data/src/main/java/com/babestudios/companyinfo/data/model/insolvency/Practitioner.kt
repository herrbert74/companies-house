package com.babestudios.companyinfo.data.model.insolvency


import android.os.Parcelable
import com.babestudios.companyinfo.data.model.common.Address
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Practitioner(
		@SerializedName("address")
		var address: Address? = null,
		@SerializedName("appointed_on")
		var appointedOn: String? = null,
		@SerializedName("ceased_to_act_on")
		var ceasedToActOn: String? = null,
		@SerializedName("name")
		var name: String? = null,
		@SerializedName("role")
		var role: String? = null
) : Parcelable
