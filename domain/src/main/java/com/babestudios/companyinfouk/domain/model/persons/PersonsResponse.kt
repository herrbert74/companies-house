package com.babestudios.companyinfouk.domain.model.persons


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonsResponse(
	var startIndex: Long? = null,
	var activeCount: Long? = null,
	var items: List<Person> = emptyList(),
	var ceasedCount: Long? = null,
	var itemsPerPage: Long? = null,
	var totalResults: Long = 0,
) : Parcelable
