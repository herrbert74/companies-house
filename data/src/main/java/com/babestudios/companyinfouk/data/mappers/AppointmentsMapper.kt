package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.core.mappers.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.model.officers.AppointedToDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.domain.model.officers.AppointedTo
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse

fun mapAppointmentsResponseDto(
	input: AppointmentsResponseDto,
	constantsHelper: ConstantsHelperContract,
): AppointmentsResponse {
	return AppointmentsResponse(
		mapMonthYearDto(input.dateOfBirth),
		mapAppointmentList(input.items, constantsHelper),
		input.name,
		input.totalResults,
	)
}

private fun mapAppointmentList(appointments: List<AppointmentDto>?, constantsHelper: ConstantsHelperContract) =
	mapNullInputList(appointments) { appointmentDto ->
		mapAppointmentDto(
			appointmentDto,
			constantsHelper,
		)
	}

private fun mapAppointmentDto(input: AppointmentDto?, constantsHelper: ConstantsHelperContract) =
	Appointment(
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

private fun mapAppointedToDto(input: AppointedToDto?): AppointedTo =
	AppointedTo(
		input?.companyName ?: "",
		input?.companyNumber ?: "",
		input?.companyStatus ?: "",
	)
