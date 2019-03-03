package com.babestudios.companyinfouk.ui.officerappointments.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment

class OfficerAppointmentsTypeFactory : OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory {
	override fun type(appointment: Appointment): Int = R.layout.row_officer_appointments

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_officer_appointments -> OfficerAppointmentsViewHolder(view)
			else -> throw RuntimeException("Illegal view type")
		}
	}
}