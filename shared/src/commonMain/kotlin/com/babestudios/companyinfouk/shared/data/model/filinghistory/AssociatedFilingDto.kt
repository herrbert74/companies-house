package com.babestudios.companyinfouk.shared.data.model.filinghistory

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AssociatedFilingDto(

	var type: String? = null,
	var description: String? = null,
	var date: String? = null,

	@SerialName("action_date")
	var actionDate: Long? = null,

	)
