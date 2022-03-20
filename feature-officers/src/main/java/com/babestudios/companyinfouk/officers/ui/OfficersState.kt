package com.babestudios.companyinfouk.officers.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsVisitableBase
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersVisitableBase


data class OfficersState(
		//Officers
	val officersRequest: Async<OfficersResponse> = Uninitialized,
	val officerItems: List<OfficersVisitableBase> = emptyList(),
	val totalOfficersCount: Int = 0,
	@PersistState
		val companyNumber: String = "",

		//Officer details
	@PersistState
		val selectedOfficer: Officer? = null,
	@PersistState
		val selectedOfficerId: String = "",

		//Officer appointments
	@PersistState
		val appointmentItems: List<OfficerAppointmentsVisitableBase> = emptyList(),
	val totalAppointmentsCount: Int = 0,
	@PersistState
		val officerName : String = "",
	val officerAppointmentsRequest: Async<AppointmentsResponse> = Uninitialized
) : MvRxState
