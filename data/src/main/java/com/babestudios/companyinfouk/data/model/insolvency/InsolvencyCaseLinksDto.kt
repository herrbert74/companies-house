package com.babestudios.companyinfouk.data.model.insolvency


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InsolvencyCaseLinksDto (
	@SerializedName("charge")
	var charge: String? = null
):Parcelable
