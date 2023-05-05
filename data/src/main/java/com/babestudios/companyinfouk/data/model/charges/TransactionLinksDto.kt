package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class TransactionLinksDto(
		@SerialName("filing")
		var filing: String? = null,
		@SerialName("insolvency_case")
		var insolvencyCase: String? = null
) : Parcelable
