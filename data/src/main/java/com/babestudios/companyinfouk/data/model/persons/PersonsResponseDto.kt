package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.SelfLinkDataDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class PersonsResponseDto(
		@SerializedName("start_index")
		var startIndex: Long? = null,
		@SerializedName("links")
		var links: SelfLinkDataDto? = null,
		@SerializedName("active_count")
		var activeCount: Long? = null,
		@SerializedName("items")
		var items: List<PersonDto> = ArrayList(),
		@SerializedName("ceased_count")
		var ceasedCount: Long? = null,
		@SerializedName("items_per_page")
		var itemsPerPage: Long? = null,
		@SerializedName("total_results")
		var totalResults: Long? = null
) : Parcelable
