package com.babestudios.companyinfouk.data.model.insolvency

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
class InsolvencyCaseDto (
		@SerializedName("dates")
	var dates: List<DateDto> = ArrayList(),
		@SerializedName("links")
	var links: InsolvencyCaseLinksDto? = null,
		@SerializedName("notes")
	var notes: List<String> = ArrayList(),
		@SerializedName("number")
	var number: String? = null,
		@SerializedName("practitioners")
	var practitioners: List<PractitionerDto> = ArrayList(),
		@SerializedName("type")
	var type: String? = null
): Parcelable
