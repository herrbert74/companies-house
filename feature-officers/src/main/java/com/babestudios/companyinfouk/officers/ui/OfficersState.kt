package com.babestudios.companyinfouk.officers.ui

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.officers.ui.appointments.list.AbstractOfficerAppointmentsVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.AbstractOfficersVisitable


data class OfficersState(
		//Officers
		val officersRequest: Async<Officers> = Uninitialized,
		val officerItems: List<AbstractOfficersVisitable> = emptyList(),
		val totalOfficersCount: Int = 0,
		@PersistState
		val companyNumber: String = "",

		//Officer details
		@PersistState
		val officerItem: OfficerItem? = null,
		@PersistState
		val officerId: String = "",

		//Officer appointments
		@PersistState
		val appointmentItems: List<AbstractOfficerAppointmentsVisitable> = emptyList(),
		val totalAppointmentsCount: Int = 0,
		@PersistState
		val officerName : String = "",
		val officerAppointmentsRequest: Async<Appointments> = Uninitialized
) : MvRxState
