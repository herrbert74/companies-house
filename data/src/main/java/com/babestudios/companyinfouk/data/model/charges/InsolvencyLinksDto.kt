package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class InsolvencyLinksDto : Parcelable {
	inner class Links {

		@SerializedName("case")
		var case: String? = null

	}
}
