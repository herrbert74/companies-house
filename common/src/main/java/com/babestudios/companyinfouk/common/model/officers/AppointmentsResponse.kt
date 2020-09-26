package com.babestudios.companyinfouk.common.model.officers


import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear

data class AppointmentsResponse(
		var dateOfBirth: MonthYear? = null,
		var items: List<Appointment>? = null,
		var name: String? = null,
		var totalResults: String? = null,
)

data class Appointment(
		var address: Address = Address(),
		var appointedOn: String = "",
		var appointedTo: AppointedTo = AppointedTo(),
		var countryOfResidence: String? = null,
		var name: String = "",
		var nationality: String? = null,
		var occupation: String? = null,
		var officerRole: String = "",
		var resignedOn: String? = null
)

class AppointedTo(
		var companyName: String = "",
		var companyNumber: String = "",
		var companyStatus: String = "",
)
