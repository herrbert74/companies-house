package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.View
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.officers.databinding.RowOfficerAppointmentsBinding

class OfficerAppointmentsViewHolder(_binding: ViewBinding) 
	: BaseViewHolder<AbstractOfficerAppointmentsVisitable>(_binding) {
	override fun bind(visitable: AbstractOfficerAppointmentsVisitable) {
		val binding = _binding as RowOfficerAppointmentsBinding
		val appointment = (visitable as OfficerAppointmentsVisitable).appointment
		binding.lblOfficerAppointmentsAppointedOn.text = appointment.appointedOn
		binding.lblOfficerAppointmentsCompanyName.text = appointment.appointedTo?.companyName
		binding.lblOfficerAppointmentsCompanyStatus.text = appointment.appointedTo?.companyStatus
		binding.lblOfficerAppointmentsRole.text = appointment.officerRole
		if (appointment.resignedOn != null) {
			binding.lblOfficerAppointmentsResignedOn.visibility = View.VISIBLE
			binding.cpnOfficerAppointmentsResignedOn.visibility = View.VISIBLE
			binding.lblOfficerAppointmentsResignedOn.text = appointment.resignedOn
		} else {
			binding.lblOfficerAppointmentsResignedOn.visibility = View.GONE
			binding.cpnOfficerAppointmentsResignedOn.visibility = View.GONE
		}
	}
}
