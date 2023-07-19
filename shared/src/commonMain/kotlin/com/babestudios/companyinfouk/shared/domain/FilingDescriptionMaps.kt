package com.babestudios.companyinfouk.shared.domain

/**
 * This file was converted from a json resource, which was downloaded from
 * https://github.com/companieshouse/api-enumerations, and converted from yaml files.
 * This was done to simplify KMP resource handling, as moko-resources does not work with configuration cache:
 * https://github.com/icerockdev/moko-resources/issues/311
 */
object FilingDescriptionMaps {

	val descriptionIdentifiers = mapOf(
		"abridged-accounts" to "Abridged accounts made up to {period_end_on}",
		"small-full-accounts" to "Accounts made up to {period_end_on}",
		"change-registered-office-address" to "Change of registered office address"
	)

}
