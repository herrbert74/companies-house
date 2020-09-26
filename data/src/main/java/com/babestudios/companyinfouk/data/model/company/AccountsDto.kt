package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.DayMonthDto
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountsDto(
		@SerializedName("accounting_reference_date")
		var accountingReferenceDate: DayMonthDto? = null,

		@SerializedName("last_accounts")
		var lastAccounts: LastAccountsDto? = null,

		@SerializedName("next_due")
		var nextDue: String? = null,

		var overdue: Boolean = false,

		@SerializedName("next_made_up_to")
		var nextMadeUpTo: String? = null
) : Parcelable
