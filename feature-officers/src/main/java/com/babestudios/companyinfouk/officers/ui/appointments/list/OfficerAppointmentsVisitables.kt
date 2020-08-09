package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import kotlinx.android.parcel.Parcelize

sealed class OfficerAppointmentsVisitableBase : Parcelable {
	abstract fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int
}

@Parcelize
class OfficerAppointmentsVisitable(val appointment: Appointment) : OfficerAppointmentsVisitableBase(), Parcelable {
	override fun type(officerAppointmentsTypeFactory: OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory): Int {
		return officerAppointmentsTypeFactory.type(appointment)
	}
}
