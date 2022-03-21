package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class InsolvencyLinksDto : Parcelable {
	inner class Links {

		@SerializedName("case")
		var case: String? = null

	}
}
