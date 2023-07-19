package com.babestudios.companyinfouk.data.mappers

import com.babestudios.base.data.mapNullInputList
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.shared.data.model.officers.AppointedToDto
import com.babestudios.companyinfouk.shared.data.model.officers.AppointmentDto
import com.babestudios.companyinfouk.shared.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointedTo
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse

fun AppointmentsResponseDto.toAppointmentsResponse() = AppointmentsResponse(
	dateOfBirth.toMonthYear(),
	mapAppointmentList(items),
	name,
	totalResults,
)

private fun mapAppointmentList(appointments: List<AppointmentDto>?) =
	mapNullInputList(appointments) { appointmentDto ->
		appointmentDto.toAppointment()
	}

private fun AppointmentDto?.toAppointment() = Appointment(
	this?.address.toAddress(),
	this?.appointedOn ?: "Unknown",
	this?.appointedTo.mapAppointedToDto(),
	this?.countryOfResidence,
	this?.name ?: "",
	this?.nationality,
	this?.occupation,
	ConstantsHelper.officerRoleLookup(this?.officerRole ?: ""),
	this?.resignedOn,
)

private fun AppointedToDto?.mapAppointedToDto() = AppointedTo(
	this?.companyName ?: "",
	this?.companyNumber ?: "",
	this?.companyStatus ?: "",
)
