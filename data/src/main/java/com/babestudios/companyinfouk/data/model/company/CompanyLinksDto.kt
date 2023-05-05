package com.babestudios.companyinfouk.data.model.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyLinksDto(

	@SerialName("charges")
	var charges: String? = null,

	@SerialName("filing_history")
	var filingHistory: String? = null,

	@SerialName("insolvency")
	var insolvency: String? = null,

	@SerialName("officers")
	var officers: String? = null,

	@SerialName("persons_with_significant_control")
	var personsWithSignificantControl: String? = null,

	@SerialName("persons_with_significant_control_statements")
	var personsWithSignificantControlStatements: String? = null,

	@SerialName("registers")
	var registers: String? = null,

	@SerialName("self")
	var self: String? = null,

	)
