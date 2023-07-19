package com.babestudios.companyinfouk.data.utils

import com.babestudios.companyinfouk.shared.domain.ACCOUNT_DATE_PLACEHOLDER
import com.babestudios.companyinfouk.shared.domain.ACCOUNT_TYPE_PLACEHOLDER
import com.babestudios.companyinfouk.shared.domain.COMPANY_ACCOUNTS_FORMATTED_TEXT
import com.babestudios.companyinfouk.shared.domain.COMPANY_ACCOUNTS_NOT_FOUND
import com.babestudios.companyinfouk.shared.domain.FROM_PLACEHOLDER
import com.babestudios.companyinfouk.shared.domain.OFFICER_ITEM_APPOINTED_FROM
import com.babestudios.companyinfouk.shared.domain.OFFICER_ITEM_APPOINTED_FROM_TO
import com.babestudios.companyinfouk.shared.domain.TO_PLACEHOLDER

/**
 * This class used to read string resources, and format them with parameters. Now only the formatting left,
 * after moving to constants. Whenever reading is needed again, look up the history.
 * This is now blocked by moko-resources:
 * https://github.com/icerockdev/moko-resources/issues/311
 */
object StringResourceHelper {

	fun getLastAccountMadeUpToString(accountType: String, date: String) =
		COMPANY_ACCOUNTS_FORMATTED_TEXT
			.replace(ACCOUNT_TYPE_PLACEHOLDER, accountType)
			.replace(ACCOUNT_DATE_PLACEHOLDER, date)

	fun getCompanyAccountsNotFoundString() = COMPANY_ACCOUNTS_NOT_FOUND

	fun getAppointedFromToString(from: String, to: String) =
		OFFICER_ITEM_APPOINTED_FROM_TO
			.replace(FROM_PLACEHOLDER, from)
			.replace(TO_PLACEHOLDER, to)

	fun getAppointedFromString(from: String) = OFFICER_ITEM_APPOINTED_FROM.replace(FROM_PLACEHOLDER, from)

}
