package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class LastAccounts(
		@SerializedName("made_up_to")
		var madeUpTo: String? = null,

		@SerializedName("type")
		var type: String? = null
) : Parcelable

