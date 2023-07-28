package com.babestudios.companyinfouk.shared.data.mapper

import com.babestudios.base.kotlin.ext.parseDate
import com.babestudios.base.kotlin.ext.toDateString
import com.babestudios.companyinfouk.shared.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.shared.data.util.StringResourceHelper
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.data.model.common.AddressDto
import com.babestudios.companyinfouk.shared.data.model.company.AccountsDto
import com.babestudios.companyinfouk.shared.data.model.company.CompanyDto

fun CompanyDto.toCompany() = Company(
	companyName ?: "",
	accounts.toAccountsString(),
	companyNumber ?: "",
	dateOfCreation ?: "",
	hasCharges,
	hasInsolvencyHistory,
	registeredOfficeAddress.toAddress(),
	sicCodes.mapNatureOfBusiness()
)

private fun AccountsDto?.toAccountsString(): String {
	return this?.lastAccounts?.madeUpTo?.let {
		val madeUpToDate = it.parseDate()?.toDateString()
		//madeUpToDate.let { date ->
			//val formattedDate = date.time.formatShortDateFromTimeStampMillis()
			StringResourceHelper.getLastAccountMadeUpToString(
				ConstantsHelper.accountTypeLookUp(lastAccounts?.type ?: ""),
				madeUpToDate.toString()
			)
		//} ?: StringResourceHelper.getCompanyAccountsNotFoundString()
	} ?: StringResourceHelper.getCompanyAccountsNotFoundString()
}

internal fun AddressDto?.toAddress() = Address(
	this?.addressLine1.orEmpty(),
	this?.addressLine2,
	this?.country.orEmpty(),
	this?.locality.orEmpty(),
	this?.postalCode.orEmpty(),
	this?.region,
)

private fun List<String>?.mapNatureOfBusiness(): String {
	return if (this?.isNotEmpty() == true) {
		"${this[0]} ${ConstantsHelper.sicLookUp(this[0])}"
	} else {
		"No data"
	}
}
