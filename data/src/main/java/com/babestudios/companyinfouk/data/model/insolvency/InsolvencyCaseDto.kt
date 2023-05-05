package com.babestudios.companyinfouk.data.model.insolvency

import androidx.annotation.Keep
import java.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
class InsolvencyCaseDto(
	@SerialName("dates")
	var dates: List<DateDto> = ArrayList(),
	@SerialName("links")
	var links: InsolvencyCaseLinksDto? = null,
	@SerialName("notes")
	var notes: List<String> = ArrayList(),
	@SerialName("number")
	var number: String? = null,
	@SerialName("practitioners")
	var practitioners: List<PractitionerDto> = ArrayList(),
	@SerialName("type")
	var type: String? = null,
)
