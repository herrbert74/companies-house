package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.ext.convertToTimestamp
import com.babestudios.base.ext.formatShortDateFromTimeStampMillis
import com.babestudios.base.ext.parseMySqlDate
import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.company.AccountsDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract

inline fun mapCompanyDto(
		input: CompanyDto,
		mapAccounts: (AccountsDto?) -> String,
		mapAddress: (AddressDto?) -> Address,
		mapNatureOfBusiness: (List<String>?) -> String
): Company {
	return Company(
			input.companyName ?: "",
			mapAccounts(input.accounts),
			input.companyNumber ?: "",
			input.dateOfCreation ?: "",
			input.hasCharges,
			input.hasInsolvencyHistory,
			mapAddress(input.registeredOfficeAddress),
			mapNatureOfBusiness(input.sicCodes)
	)
}

fun mapAccountsDto(
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

fun mapAddressDto(
		input: AddressDto?,
): Address {
	return Address(
			input?.addressLine1.orEmpty(),
			input?.addressLine2.orEmpty(),
			input?.country.orEmpty(),
			input?.locality.orEmpty(),
			input?.postalCode.orEmpty(),
			input?.region.orEmpty(),
	)
}

fun mapNatureOfBusiness(
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
