package com.babestudios.companyinfo.data.model.company

import android.os.Parcelable
import com.babestudios.companyinfo.data.model.common.DayMonth
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Accounts(
		@SerializedName("accounting_reference_date")
		var accountingReferenceDate: DayMonth? = null,

		@SerializedName("last_accounts")
		var lastAccounts: LastAccounts? = null,

		@SerializedName("next_due")
		var nextDue: String? = null,

		var overdue: Boolean = false,

		@SerializedName("next_made_up_to")
		var nextMadeUpTo: String? = null
) : Parcelable
