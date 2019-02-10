package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ForeignCompanyAccounts (
	@SerializedName("account_period_from")
	var accountPeriodFrom: AccountPeriodFrom? = null,

	@SerializedName("account_period_to")
	var accountPeriodTo: AccountPeriodTo? = null,

	@SerializedName("must_file_within")
	var mustFileWithin: MustFileWithin? = null
):Parcelable
