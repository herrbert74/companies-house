package com.babestudios.companyinfouk.data.utils

import android.content.Context
import com.babestudios.companyinfouk.data.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


interface StringResourceHelperContract {
	fun getLastAccountMadeUpToString(accountType: String, date: String): String
	fun getCompanyAccountsNotFoundString(): String
	fun getAppointedFromToString(from: String, to: String): String
	fun getAppointedFromString(from: String): String
	fun getInsolvencyDatesString(): String
	fun getPractitionerString(): String
	fun getRecentSearchesString(): String
}

class StringResourceHelper @Inject constructor(
	@ApplicationContext val context: Context
) : StringResourceHelperContract {

	override fun getLastAccountMadeUpToString(accountType: String, date: String): String {
		return String.format(
			context.resources.getString(R.string.company_accounts_formatted_text),
			accountType,
			date
		).replace("  ", " ")
	}

	override fun getCompanyAccountsNotFoundString(): String {
		return context.resources.getString(R.string.company_accounts_not_found)
	}

	override fun getAppointedFromToString(from: String, to: String): String {
		return String.format(context.getString(R.string.officer_item_appointed_from_to), from, to)
	}

	override fun getAppointedFromString(from: String): String {
		return String.format(context.getString(R.string.officer_item_appointed_from), from)
	}

	override fun getInsolvencyDatesString(): String {
		return context.resources.getString(R.string.insolvency_dates)
	}

	override fun getPractitionerString(): String {
		return context.resources.getString(R.string.insolvency_practitioners)
	}

	override fun getRecentSearchesString(): String {
		return context.getString(R.string.recent_searches)
	}

}
