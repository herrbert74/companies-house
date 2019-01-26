package com.babestudios.companyinfouk.data.model.charges


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class SecuredDetails : Parcelable {
	@SerializedName("description")
	var description: String? = null
	@SerializedName("type")
	var type: String? = null
}
