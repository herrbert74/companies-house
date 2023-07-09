package com.babestudios.companyinfouk.shared.domain.model.persons

data class PersonsResponse(
	var startIndex: Long? = null,
	var activeCount: Long? = null,
	var items: List<Person> = emptyList(),
	var ceasedCount: Long? = null,
	var itemsPerPage: Long? = null,
	var totalResults: Long = 0,
)
