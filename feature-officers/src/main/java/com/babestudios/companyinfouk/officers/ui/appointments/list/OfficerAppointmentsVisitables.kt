package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import kotlinx.parcelize.Parcelize

sealed class OfficerAppointmentsVisitableBase : Parcelable {
	abstract fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int
}

@Parcelize
class OfficerAppointmentsVisitable(val appointment: Appointment) : OfficerAppointmentsVisitableBase() {
	override fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int {
		return officerAppointmentsTypeFactory.type(appointment)
	}
}
