package com.babestudios.companyinfouk.data.utils

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.companyinfouk.data.R
import javax.inject.Inject


interface StringResourceHelperContract {
	fun getLastAccountMadeUpToString(accountType: String, date: String): String
	fun getCompanyAccountsNotFoundString(): String
}

class StringResourceHelper @Inject constructor(@ApplicationContext val context: Context)
	: StringResourceHelperContract {
	override fun getLastAccountMadeUpToString(accountType: String, date: String): String {
		return String.format(
				context.resources.getString(R.string.company_accounts_formatted_text),
				accountType,
				date
		)
	}

	override fun getCompanyAccountsNotFoundString(): String {
		return context.resources.getString(R.string.company_accounts_not_found)
	}
}
