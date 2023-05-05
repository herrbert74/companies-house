package com.babestudios.companyinfouk.data.model.company

import com.babestudios.companyinfouk.data.model.common.DayMonthDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ForeignCompanyAccountsDto(
	@SerialName("account_period_from")
	var accountPeriodFrom: DayMonthDto? = null,

	@SerialName("account_period_to")
	var accountPeriodTo: DayMonthDto? = null,

	@SerialName("must_file_within")
	var mustFileWithin: MustFileWithinDto? = null,
)
