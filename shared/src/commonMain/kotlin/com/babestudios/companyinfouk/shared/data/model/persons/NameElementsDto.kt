package com.babestudios.companyinfouk.shared.data.model.persons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class NameElementsDto(
	@SerialName("surname")
	var surname: String? = null,
	@SerialName("title")
	var title: String? = null,
	@SerialName("middle_name")
	var middleName: String? = null,
	@SerialName("forename")
	var forename: String? = null,
)
