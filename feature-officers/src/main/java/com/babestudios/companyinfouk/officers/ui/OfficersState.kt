package com.babestudios.companyinfouk.officers.ui

import com.airbnb.mvrx.MvRxState
import com.babestudios.base.mvp.BaseState
import com.babestudios.base.mvrx.ScreenState
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.officers.ui.appointments.list.AbstractOfficerAppointmentsVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.AbstractOfficersVisitable


data class OfficersState(
		//Officers
		val officerItems: List<AbstractOfficersVisitable> = emptyList(),
		val totalOfficersCount: Int = 0,
		val companyNumber: String = "",
		val officersScreenState: ScreenState = ScreenState.Initialized,

		//Officer details
		val officerItem: OfficerItem? = null,
		val officerId: String = "",
		val officerDetailsScreenState: ScreenState = ScreenState.Initialized,

		//Officer appointments
		val appointmentItems: List<AbstractOfficerAppointmentsVisitable> = emptyList(),
		val totalAppointmentsCount: Int = 0,
		val officerName : String = "",
		val officerAppointmentsScreenState: ScreenState = ScreenState.Initialized
) : MvRxState