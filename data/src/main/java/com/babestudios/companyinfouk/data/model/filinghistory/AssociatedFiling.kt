package com.babestudios.companyinfouk.data.model.filinghistory


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class AssociatedFiling(
		@SerializedName("description_values")
		var descriptionValues: DescriptionValues? = null,
		@SerializedName("type")
		var type: String? = null,
		@SerializedName("description")
		var description: String? = null,
		@SerializedName("data")
		var data: @RawValue Any? = null,
		@SerializedName("date")
		var date: String? = null,
		@SerializedName("action_date")
		var actionDate: Long? = null,
		@SerializedName("original_description")
		var originalDescription: String? = null,
		@SerializedName("category")
		var category: String? = null
) : Parcelable
