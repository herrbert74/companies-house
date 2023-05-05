package com.babestudios.companyinfouk.data.model.persons

import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import java.util.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class PersonsResponseDto(
	@SerialName("start_index")
	var startIndex: Long? = null,
	@SerialName("links")
	var links: SelfLinkDataDto? = null,
	@SerialName("active_count")
	var activeCount: Long? = null,
	@SerialName("items")
	var items: List<PersonDto> = ArrayList(),
	@SerialName("ceased_count")
	var ceasedCount: Long? = null,
	@SerialName("items_per_page")
	var itemsPerPage: Long? = null,
	@SerialName("total_results")
	var totalResults: Long? = null,
)
