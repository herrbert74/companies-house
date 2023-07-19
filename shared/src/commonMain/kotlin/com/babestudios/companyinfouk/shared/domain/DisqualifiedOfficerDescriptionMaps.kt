package com.babestudios.companyinfouk.shared.domain

/**
 * This file was converted from a json resource, which was downloaded from
 * https://github.com/companieshouse/api-enumerations, and converted from yaml files.
 * This was done to simplify KMP resource handling, as moko-resources does not work with configuration cache:
 * https://github.com/icerockdev/moko-resources/issues/311
 */
object DisqualifiedOfficerDescriptionMaps {

	val descriptionIdentifier = mapOf(
		"conviction-of-indictable-offence" to "Disqualification on conviction of indictable offence",
		"persistent-breaches-of-companies-legislation" to "Disqualification for persistent breaches of companies legislation",
		"fraud-etc-in-winding-up" to "Disqualification for fraud, etc., in winding up",
		"summary-conviction" to "Disqualification on summary conviction",
		"court-to-disqualify-unfit-directors-of-insolvent-companies" to
			"Duty of court to disqualify unfit directors of insolvent companies",
		"order-or-undertaking-and-reporting-provisions" to "Disqualification order or undertaking; and reporting provisions",
		"investigation-of-company" to "Disqualification after investigation of company",
		"matters-determining-unfitness-of-directors" to "Matters for determining unfitness of directors",
		"competition-infringements" to "Disqualification for competition infringements",
		"undertaking-for-competition" to "Disqualification undertaking for competition",
		"participation-in-wrongful-trading" to "Participation in wrongful trading",
		"conviction-of-offence-punishable-on-indictment-or-conviction-on-indictment-or-summary-conviction" to
			"Disqualification on conviction of offence punishable only on indictment or either on conviction on " +
			"indictment or on summary conviction",
		"persistent-default-under-companies-legislation" to
			"Disqualification for persistent default under companies legislation",
		"summary-conviction-of-offence" to "Disqualification on summary conviction of offence",
		"high-court-to-disqualify-unfit-directors-of-insolvent-companies" to
			"Duty of High Court to disqualify unfit directors of insolvent companies",
		"competition-disqualification-order" to "Competition Disqualification Order",
		"certain-convictions-abroad" to "Disqualification for certain convictions abroad",
		"undertaking-instead-of-order-under-article-11a" to
			"Disqualification undertaking instead of an order under Article 11A",
		"undertaking-instead-of-order-under-article-11d" to
			"Disqualification undertaking instead of an order under Article 11D",
		"order-disqualifying-a-person-instructing-an-unfit-director-of-an-insolvent-company" to
			"Order disqualifying a person instructing an unfit director of an insolvent company",
		"order-disqualifying-a-person-instructing-an-unfit-director" to
			"Order disqualifying a person instructing an unfit director",
		"undertaking-disqualifying-a-person-instructing-an-unfit-director-of-an-insolvent-company" to
			"Disqualification undertaking disqualifying a person instructing an unfit director of an insolvent company",
		"undertaking-disqualifying-a-person-instructing-an-unfit-director" to
			"Disqualification undertaking disqualifying a person instructing an unfit director"
	)

	val act = mapOf(
		"company-directors-disqualification-act-1986" to "Company Directors Disqualification Act 1986",
		"company-directors-disqualification-northern-ireland-order-2002" to
			"Company Directors Disqualification (Northern Ireland) Order 2002"
	)

	val disqualificationType = mapOf(
		"court-order" to "Court order",
		"undertaking" to "Undertaking"
	)

}
