@file:Suppress("MatchingDeclarationName")

package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers.databinding.RowOfficerAppointmentsBinding

class AppointmentsViewHolder(val rawBinding: ViewBinding) : RecyclerView.ViewHolder(rawBinding.root) {

	fun bind(appointment: Appointment) {
		val binding = rawBinding as RowOfficerAppointmentsBinding
		binding.lblOfficerAppointmentsAppointedOn.text = appointment.appointedOn
		binding.lblOfficerAppointmentsCompanyName.text = appointment.appointedTo.companyName
		binding.lblOfficerAppointmentsCompanyStatus.text = appointment.appointedTo.companyStatus
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
