package com.babestudios.companyinfouk.data.model.company


import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.DayMonthDto
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class ForeignCompanyAccountsDto (
		@SerialName("account_period_from")
	var accountPeriodFrom: DayMonthDto? = null,

		@SerialName("account_period_to")
	var accountPeriodTo: DayMonthDto? = null,

		@SerialName("must_file_within")
	var mustFileWithin: MustFileWithinDto? = null
):Parcelable
