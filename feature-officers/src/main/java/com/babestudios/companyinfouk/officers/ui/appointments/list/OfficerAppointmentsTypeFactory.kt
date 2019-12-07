package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import com.babestudios.companyinfouk.officers.R
import java.lang.IllegalStateException

class OfficerAppointmentsTypeFactory : OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory {
	override fun type(appointment: Appointment): Int = R.layout.row_officer_appointments

	override fun holder(type: Int, view: View): BaseViewHolder<*> {
		return when (type) {
			R.layout.row_officer_appointments -> OfficerAppointmentsViewHolder(view)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
