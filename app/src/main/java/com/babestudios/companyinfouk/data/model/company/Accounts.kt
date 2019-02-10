package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Accounts(
		@SerializedName("accounting_reference_date")
		var accountingReferenceDate: AccountingReferenceDate? = null,

		@SerializedName("last_accounts")
		var lastAccounts: LastAccounts? = null,

		@SerializedName("next_due")
		var nextDue: String? = null,

		var overdue: Boolean = false,

		@SerializedName("next_made_up_to")
		var nextMadeUpTo: String? = null
) : Parcelable
