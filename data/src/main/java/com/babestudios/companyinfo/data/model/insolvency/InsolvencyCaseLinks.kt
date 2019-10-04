package com.babestudios.companyinfo.data.model.insolvency


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InsolvencyCaseLinks (
	@SerializedName("charge")
	var charge: String? = null
):Parcelable
