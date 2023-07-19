package com.babestudios.companyinfouk.shared.domain.model

/**
 * This file was converted from a json resource, which was downloaded from
 * https://github.com/companieshouse/api-enumerations, and converted from yaml files.
 * This was done to simplify KMP resource handling, as moko-resources does not work with configuration cache:
 * https://github.com/icerockdev/moko-resources/issues/311
 */
object ExemptionDescriptionsMaps {

	val exemptionType = mapOf(
		"disclosure-transparency-rules-chapter-five-applies" to
			"This is a traded, DTR5 issuing company. Therefore it may be exempt from updating its PSC information.",
		"psc-exempt-as-trading-on-regulated-market" to
			"The company has been or is exempt from keeping a PSC register, as it has voting shares admitted to " +
			"trading on a regulated market other than the UK.",
		"psc-exempt-as-shares-admitted-on-market" to
			"The company has been or is exempt from keeping a PSC register, as it has voting shares admitted to " +
			"trading on a market listed in the Register of People with Significant Control Regulations 2016.",
		"psc-exempt-as-trading-on-uk-regulated-market" to
			"The company has been or is exempt from keeping a PSC register, as it has voting shares admitted to " +
			"trading on a UK regulated market."
	)

	val exemptionDescription = mapOf(
		"disclosure-transparency-rules-chapter-five-applies" to
			"From {exempt_from} this is a traded, DTR5 issuing company. Therefore it may be exempt from updating its " +
			"PSC information.",
		"psc-exempt-as-trading-on-regulated-market" to
			"From {exempt_from} the company is exempt from keeping a PSC register as it has voting shares admitted to " +
			"trading on a regulated market other than the UK",
		"psc-exempt-as-shares-admitted-on-market" to
			"From {exempt_from} the company is exempt from keeping a PSC register as it has voting shares admitted to " +
			"trading on a market listed in the Register of People with Significant Control Regulations 2016",
		"psc-exempt-as-trading-on-uk-regulated-market" to
			"From {exempt_from} the company is exempt from keeping a PSC register as it has voting shares admitted to " +
			"trading on a UK regulated market"
	)

}
