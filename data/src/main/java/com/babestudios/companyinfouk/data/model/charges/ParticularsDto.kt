package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParticularsDto(
		@SerializedName("chargor_acting_as_bare_trustee")
		var chargorActingAsBareTrustee: String? = null,
		@SerializedName("contains_fixed_charge")
		var containsFixedCharge: Boolean?,
		@SerializedName("contains_floating_charge")
		var containsFloatingCharge: Boolean?,
		@SerializedName("contains_negative_pledge")
		var containsNegativePledge: Boolean?,
		@SerializedName("description")
		var description: String? = null,
		@SerializedName("floating_charge_covers_all")
		var floatingChargeCoversAll: Boolean?,
		@SerializedName("type")
		var type: String? = null
) : Parcelable
