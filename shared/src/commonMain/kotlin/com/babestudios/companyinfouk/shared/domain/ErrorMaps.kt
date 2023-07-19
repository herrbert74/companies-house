package com.babestudios.companyinfouk.shared.domain

/**
 * This file was converted from a json resource, which was downloaded from
 * https://github.com/companieshouse/api-enumerations, and converted from yaml files.
 * This was done to simplify KMP resource handling, as moko-resources does not work with configuration cache:
 * https://github.com/icerockdev/moko-resources/issues/311
 */
object ErrorMaps {

	val service = mapOf(
		"access-denied" to "Access denied",
		"company-profile-not-found" to "Company profile not found",
		"company-insolvencies-not-found" to "Company insolvencies not found",
		"invalid-authorization-header" to "Invalid authorization header",
		"invalid-http-method" to "Access denied for HTTP method {method}",
		"invalid-client-id" to "Invalid client ID",
		"no-json-provided" to "No JSON payload provided",
		"not-authorised-for-company" to "Not authorised to file for this company",
		"transaction-not-open" to "Transaction is not open",
		"transaction-does-not-exist" to "Transaction does not exist",
		"user-transactions-not-found" to "No transactions found for this user",
		"unauthorised" to "Unauthorised"
	)

}
