package com.babestudios.companyinfouk.shared.domain.model.persons

import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.common.MonthYear
import kotlinx.serialization.Serializable

@Serializable
data class Person(
	val notifiedOn: String,
	val ceasedOn: String? = null,
	val kind: String,
	val countryOfResidence: String? = null,
	val dateOfBirth: MonthYear = MonthYear(null, null),
	val address: Address,
	val naturesOfControl: List<String>,
	val nationality: String? = null,
	val name: String,
	val identification: Identification? = null,
)

@Serializable
data class Identification(
	val countryRegistered: String? = null,
	val legalAuthority: String,
	val legalForm: String,
	val placeRegistered: String? = null,
	val registrationNumber: String? = null,
)
