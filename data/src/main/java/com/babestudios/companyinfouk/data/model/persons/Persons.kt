package com.babestudios.companyinfouk.data.model.persons


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.SelfLinkData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Persons(
		@SerializedName("start_index")
		var startIndex: Long? = null,
		@SerializedName("links")
		var links: SelfLinkData? = null,
		@SerializedName("active_count")
		var activeCount: Long? = null,
		@SerializedName("items")
		var items: List<Person> = ArrayList(),
		@SerializedName("ceased_count")
		var ceasedCount: Long? = null,
		@SerializedName("items_per_page")
		var itemsPerPage: Long? = null,
		@SerializedName("total_results")
		var totalResults: Long? = null
) : Parcelable