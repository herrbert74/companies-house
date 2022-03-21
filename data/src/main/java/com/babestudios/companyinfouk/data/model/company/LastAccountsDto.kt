package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class LastAccountsDto(
		@SerializedName("made_up_to")
		var madeUpTo: String? = null,

		@SerializedName("type")
		var type: String? = null
) : Parcelable

