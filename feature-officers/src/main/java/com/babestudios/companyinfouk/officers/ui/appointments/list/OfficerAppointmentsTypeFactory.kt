package com.babestudios.companyinfouk.officers.ui.appointments.list

import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers.R

class OfficerAppointmentsTypeFactory : OfficerAppointmentsAdapter.OfficerAppointmentsTypeFactory {
	override fun type(appointment: Appointment): Int = R.layout.row_officer_appointments

	override fun holder(type: Int, binding: ViewBinding): BaseViewHolder<OfficerAppointmentsVisitableBase> {
		return when (type) {
			R.layout.row_officer_appointments -> OfficerAppointmentsViewHolder(binding)
			else -> throw IllegalStateException("Illegal view type")
		}
	}
}
