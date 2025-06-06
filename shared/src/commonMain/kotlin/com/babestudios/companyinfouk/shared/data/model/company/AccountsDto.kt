package com.babestudios.companyinfouk.shared.data.model.company

import com.babestudios.companyinfouk.shared.data.model.common.DayMonthDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AccountsDto(
	@SerialName("accounting_reference_date")
	var accountingReferenceDate: DayMonthDto? = null,

	@SerialName("last_accounts")
	var lastAccounts: LastAccountsDto? = null,

	@SerialName("next_accounts")
	var nextAccounts: NextAccountsDto? = null,

	@SerialName("next_due")
	var nextDue: String? = null,

	var overdue: Boolean = false,

	@SerialName("next_made_up_to")
	var nextMadeUpTo: String? = null,
)
