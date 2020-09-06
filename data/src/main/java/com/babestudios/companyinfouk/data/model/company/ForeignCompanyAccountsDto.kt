package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.DayMonth
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class ForeignCompanyAccountsDto (
		@SerializedName("account_period_from")
	var accountPeriodFrom: DayMonth? = null,

		@SerializedName("account_period_to")
	var accountPeriodTo: DayMonth? = null,

		@SerializedName("must_file_within")
	var mustFileWithin: MustFileWithinDto? = null
):Parcelable
