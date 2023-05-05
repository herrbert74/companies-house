package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class BranchCompanyDetailsDto(
		@SerialName("business_activity")
		var businessActivity: String? = null,

		@SerialName("parent_company_name")
		var parentCompanyName: String? = null,

		@SerialName("parent_company_number")
		var parentCompanyNumber: String? = null
) : Parcelable

