package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import com.babestudios.companyinfouk.officers.R

class OfficerAppointmentsTypeFactory : OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory {
	override fun type(appointment: Appointment): Int = R.layout.row_officer_appointments

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_officer_appointments -> OfficerAppointmentsViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}