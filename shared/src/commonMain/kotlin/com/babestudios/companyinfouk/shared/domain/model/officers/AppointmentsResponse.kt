package com.babestudios.companyinfouk.shared.domain.model.officers

import com.babestudios.companyinfouk.shared.domain.model.common.Address
import kotlinx.datetime.YearMonth

data class AppointmentsResponse(
	var dateOfBirth: YearMonth? = null,
	var items: List<Appointment> = emptyList(),
	var name: String = "",
	var totalResults: Int = 0,
)

data class Appointment(
	var address: Address = Address(),
	var appointedOn: String? = null,
	var appointedTo: AppointedTo = AppointedTo(),
	var countryOfResidence: String? = null,
	var name: String = "",
	var nationality: String? = null,
	var occupation: String? = null,
	var officerRole: String = "",
	var resignedOn: String? = null,
)

class AppointedTo(
	var companyName: String = "",
	var companyNumber: String = "",
	var companyStatus: String = "",
)
