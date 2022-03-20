package com.babestudios.companyinfouk.domain.model.persons


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonsResponse(
	var startIndex: Long,
	var activeCount: Long,
	var items: List<Person>,
	var ceasedCount: Long,
	var itemsPerPage: Long,
	var totalResults: Long,
) : Parcelable
