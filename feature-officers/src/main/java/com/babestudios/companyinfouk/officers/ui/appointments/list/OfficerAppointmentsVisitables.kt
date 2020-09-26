package com.babestudios.companyinfouk.officers.ui.appointments.list

import com.babestudios.companyinfouk.common.model.officers.Appointment

sealed class OfficerAppointmentsVisitableBase {
	abstract fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int
}

class OfficerAppointmentsVisitable(val appointment: Appointment) : OfficerAppointmentsVisitableBase() {
	override fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int {
		return officerAppointmentsTypeFactory.type(appointment)
	}
}
