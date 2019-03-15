package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class NameElements(
		@SerializedName("surname")
		var surname: String? = null,
		@SerializedName("title")
		var title: String? = null,
		@SerializedName("middle_name")
		var middleName: String? = null,
		@SerializedName("forename")
		var forename: String? = null
) : Parcelable
