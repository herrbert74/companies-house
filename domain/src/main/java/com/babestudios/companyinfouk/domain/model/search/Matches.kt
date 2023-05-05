package com.babestudios.companyinfouk.domain.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Matches(

	@SerialName("title")
	var title: List<Int> = ArrayList(),

	@SerialName("snippet")
	var snippet: List<String> = ArrayList(),

	)
