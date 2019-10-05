package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Particulars(
		@SerializedName("chargor_acting_as_bare_trustee")
		var chargorActingAsBareTrustee: String? = null,
		@SerializedName("contains_fixed_charge")
		var containsFixedCharge: Boolean = false,
		@SerializedName("contains_floating_charge")
		var containsFloatingCharge: Boolean = false,
		@SerializedName("contains_negative_pledge")
		var containsNegativePledge: Boolean = false,
		@SerializedName("description")
		var description: String? = null,
		@SerializedName("floating_charge_covers_all")
		var floatingChargeCoversAll: Boolean = false,
		@SerializedName("type")
		var type: String? = null
) : Parcelable
