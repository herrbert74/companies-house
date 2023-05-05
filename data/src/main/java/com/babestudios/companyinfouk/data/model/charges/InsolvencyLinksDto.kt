package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class InsolvencyLinksDto : Parcelable {
	inner class Links {

		@SerialName("case")
		var case: String? = null

	}
}
