package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScottishAlterationsDto(
		@SerializedName("has_alterations_to_order")
		var hasAlterationsToOrder: String? = null,
		@SerializedName("has_alterations_to_prohibitions")
		var hasAlterationsToProhibitions: String? = null,
		@SerializedName("has_restricting_provisions")
		var hasRestrictingProvisions: String? = null
) : Parcelable
