package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.common.model.common.MonthYear
import com.babestudios.companyinfouk.common.model.officers.AppointedTo
import com.babestudios.companyinfouk.common.model.officers.Appointment
import com.babestudios.companyinfouk.common.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.MonthYearDto
import com.babestudios.companyinfouk.data.model.officers.AppointedToDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto

inline fun mapAppointmentsResponseDto(
	input: AppointmentsResponseDto,
	mapAppointmentsDto: (List<AppointmentDto>?) -> List<Appointment>,
	mapMonthYearDto: (MonthYearDto?) -> MonthYear,
): AppointmentsResponse {
	return AppointmentsResponse(
		mapMonthYearDto(input.dateOfBirth),
		mapAppointmentsDto(input.items),
		input.name,
		input.totalResults,
	)
}

fun mapAppointmentDto(
	input: AppointmentDto?,
	mapAppointedToDto: (AppointedToDto?) -> AppointedTo,
	mapAddressDto: (AddressDto?) -> Address,
	constantsHelper: ConstantsHelperContract,
): Appointment {
	return Appointment(
		mapAddressDto(input?.address),
		input?.appointedOn ?: "Unknown",
		mapAppointedToDto(input?.appointedTo),
		input?.countryOfResidence,
		input?.name ?: "",
		input?.nationality,
		input?.occupation,
		constantsHelper.officerRoleLookup(input?.officerRole ?: ""),
		input?.resignedOn,
	)
}

fun mapAppointedToDto(
	input: AppointedToDto?,
): AppointedTo {
	return AppointedTo(
		input?.companyName ?: "",
		input?.companyNumber ?: "",
		input?.companyStatus ?: "",
	)
}
