package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.os.Parcelable
import com.babestudios.companyinfouk.common.model.officers.Appointment
import kotlinx.android.parcel.Parcelize

sealed class OfficerAppointmentsVisitableBase : Parcelable {
	abstract fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int
}

@Parcelize
class OfficerAppointmentsVisitable(val appointment: Appointment) : OfficerAppointmentsVisitableBase() {
	override fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int {
		return officerAppointmentsTypeFactory.type(appointment)
	}
}
