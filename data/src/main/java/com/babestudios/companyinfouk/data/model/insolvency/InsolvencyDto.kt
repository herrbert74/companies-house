package com.babestudios.companyinfouk.data.model.insolvency

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

@Keep
class InsolvencyDto {
	@SerializedName("cases")
	var cases: List<InsolvencyCaseDto> = ArrayList()
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("status")
	var status: List<Any> = ArrayList()
}
