package com.babestudios.companyinfouk.data.model.insolvency

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

import java.util.ArrayList
import kotlinx.serialization.Serializable

@Keep
@Serializable
class InsolvencyDto {
	@SerialName("cases")
	var cases: List<InsolvencyCaseDto> = ArrayList()
	@SerialName("etag")
	var etag: String? = null
	@SerialName("status")
	var status: List<String> = ArrayList()
}
