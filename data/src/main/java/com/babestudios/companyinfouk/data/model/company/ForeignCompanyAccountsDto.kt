package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.DayMonthDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ForeignCompanyAccountsDto (
		@SerializedName("account_period_from")
	var accountPeriodFrom: DayMonthDto? = null,

		@SerializedName("account_period_to")
	var accountPeriodTo: DayMonthDto? = null,

		@SerializedName("must_file_within")
	var mustFileWithin: MustFileWithinDto? = null
):Parcelable
