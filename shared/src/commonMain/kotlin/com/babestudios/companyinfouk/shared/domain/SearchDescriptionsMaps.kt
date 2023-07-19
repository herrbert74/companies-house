package com.babestudios.companyinfouk.shared.domain

object SearchDescriptionsMaps {

	val companySearchDescription = mapOf(
		"incorporated-on" to "Incorporated on {date:date_of_creation}",
		"registered-on" to "Registered on {date:date_of_creation}",
		"formed-on" to "Formed on {date:date_of_creation}",
		"dissolved-on" to "Dissolved on {date:date_of_cessation}",
		"converted-closed-on" to "Converted/Closed on {date:date_of_cessation}",
		"closed-on" to "Closed on {date:date_of_cessation}",
		"closed" to "Closed",
		"first-uk-establishment-opened-on" to "First UK establishment opened on {date:date_of_creation}",
		"opened-on" to "Opened on {date:date_of_creation}",
		"voluntary-arrangement" to "Voluntary Arrangement",
		"receivership" to "Receiver Action",
		"insolvency-proceedings" to "Insolvency Proceedings",
		"liquidation" to "Liquidation",
		"administration" to "In Administration",
		"registered-externally" to "Registered externally as {external_registration_number}"
	)

	val officerSearchDescription = mapOf(
		"appointment-count" to "Total number of appointments {appointment_count}",
		"born-on" to "Born {month-year:date_of_birth}"
	)

	val disqualifiedOfficerSearchDescription = mapOf(
		"born-on" to "Born on {date:date_of_birth}"
	)

}
