package com.babestudios.companyinfouk.data.model.filinghistory

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class AssociatedFilingDto(
	var type: String? = null,
	var description: String? = null,
	var date: String? = null,

	@SerialName("action_date")
	var actionDate: Long? = null,

	) : Parcelable
