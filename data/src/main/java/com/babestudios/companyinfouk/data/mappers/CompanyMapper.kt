package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.kotlin.ext.formatShortDateFromTimeStampMillis
import com.babestudios.base.kotlin.ext.parseMySqlDate
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.company.AccountsDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract

fun mapCompanyDto(
	input: CompanyDto,
	constantsHelper: ConstantsHelperContract,
	stringResourceHelper: StringResourceHelperContract,
): Company {
	return Company(
		input.companyName ?: "",
		mapAccountsDto(input.accounts, constantsHelper, stringResourceHelper),
		input.companyNumber ?: "",
		input.dateOfCreation ?: "",
		input.hasCharges,
		input.hasInsolvencyHistory,
		mapAddressDto(input.registeredOfficeAddress),
		mapNatureOfBusiness(input.sicCodes, constantsHelper)
	)
}

private fun mapAccountsDto(
	input: AccountsDto?,
	constantsHelper: ConstantsHelperContract,
	stringResourceHelper: StringResourceHelperContract,
): String {
	return input?.lastAccounts?.madeUpTo?.let {
		val madeUpToDate = it.parseMySqlDate()
		madeUpToDate?.let { date ->
			val formattedDate = date.time.formatShortDateFromTimeStampMillis()
			stringResourceHelper.getLastAccountMadeUpToString(
				constantsHelper.accountTypeLookUp(input.lastAccounts?.type ?: ""),
				formattedDate
			)
		} ?: stringResourceHelper.getCompanyAccountsNotFoundString()
	} ?: stringResourceHelper.getCompanyAccountsNotFoundString()
}

internal fun mapAddressDto(
	input: AddressDto?,
): Address {
	return Address(
		input?.addressLine1.orEmpty(),
		input?.addressLine2,
		input?.country.orEmpty(),
		input?.locality.orEmpty(),
		input?.postalCode.orEmpty(),
		input?.region,
	)
}

private fun mapNatureOfBusiness(
	input: List<String>?,
	constantsHelper: ConstantsHelperContract,
): String {
	return if (input?.isNotEmpty() == true) {
		"${input[0]} ${constantsHelper.sicLookUp(input[0])}"
	} else {
		//TODO Create a string provider to get this from strings.xml, but don't rely on context here
		"No data"
	}
}
