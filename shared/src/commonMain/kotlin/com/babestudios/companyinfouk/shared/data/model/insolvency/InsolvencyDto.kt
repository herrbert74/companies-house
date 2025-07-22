package com.babestudios.companyinfouk.shared.data.model.insolvency

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class InsolvencyDto {
	@SerialName("cases")
	var cases: List<InsolvencyCaseDto> = ArrayList()

	@SerialName("etag")
	var etag: String? = null

	@SerialName("status")
	var status: List<String> = ArrayList()
}
