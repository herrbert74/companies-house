package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ScottishAlterationsDto(
		@SerialName("has_alterations_to_order")
		var hasAlterationsToOrder: String? = null,
		@SerialName("has_alterations_to_prohibitions")
		var hasAlterationsToProhibitions: String? = null,
		@SerialName("has_restricting_provisions")
		var hasRestrictingProvisions: String? = null
) : Parcelable
