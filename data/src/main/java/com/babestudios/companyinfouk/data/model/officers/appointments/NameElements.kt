package com.babestudios.companyinfouk.data.model.officers.appointments


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NameElements(

		@SerializedName("forename")
		var forename: String? = null,
		@SerializedName("honours")
		var honours: String? = null,
		@SerializedName("other_forenames")
		var otherForenames: String? = null,
		@SerializedName("surname")
		var surname: String? = null,
		@SerializedName("title")
		var title: String? = null

) : Parcelable
