package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountingRequirement(

		@SerializedName("foreign_account_type")
		var foreignAccountType: String? = null,

		@SerializedName("terms_of_account_publication")
		var termsOfAccountPublication: String? = null
) : Parcelable
