package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class AccountingRequirementDto(

		@SerialName("foreign_account_type")
		var foreignAccountType: String? = null,

		@SerialName("terms_of_account_publication")
		var termsOfAccountPublication: String? = null
) : Parcelable
