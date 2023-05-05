package com.babestudios.companyinfouk.data.model.insolvency


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class InsolvencyCaseLinksDto (
	@SerialName("charge")
	var charge: String? = null
):Parcelable
