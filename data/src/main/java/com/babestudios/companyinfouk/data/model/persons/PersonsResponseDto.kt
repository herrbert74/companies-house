package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlinx.serialization.Serializable

@Parcelize
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
		var totalResults: Long? = null
) : Parcelable
