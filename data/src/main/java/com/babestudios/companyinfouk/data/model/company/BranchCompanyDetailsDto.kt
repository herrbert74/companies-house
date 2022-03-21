package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class BranchCompanyDetailsDto(
		@SerializedName("business_activity")
		var businessActivity: String? = null,

		@SerializedName("parent_company_name")
		var parentCompanyName: String? = null,

		@SerializedName("parent_company_number")
		var parentCompanyNumber: String? = null
) : Parcelable

