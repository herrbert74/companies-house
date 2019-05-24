package com.babestudios.companyinfouk.ui.officerappointments.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_officer_appointments.view.*

class OfficerAppointmentsViewHolder(itemView: View) : BaseViewHolder<AbstractOfficerAppointmentsVisitable>(itemView) {
	override fun bind(visitable: AbstractOfficerAppointmentsVisitable) {
		val appointment = (visitable as OfficerAppointmentsVisitable).appointment
		itemView.lblOfficerAppointmentsAppointedOn?.text = appointment.appointedOn
		itemView.lblOfficerAppointmentsCompanyName?.text = appointment.appointedTo?.companyName
		itemView.lblOfficerAppointmentsCompanyStatus?.text = appointment.appointedTo?.companyStatus
		itemView.lblOfficerAppointmentsRole?.text = appointment.officerRole
		if (appointment.resignedOn != null) {
			itemView.lblOfficerAppointmentsResignedOn?.visibility = View.VISIBLE
			itemView.cpnOfficerAppointmentsResignedOn?.visibility = View.VISIBLE
			itemView.lblOfficerAppointmentsResignedOn?.text = appointment.resignedOn
		} else {
			itemView.lblOfficerAppointmentsResignedOn?.visibility = View.GONE
			itemView.cpnOfficerAppointmentsResignedOn?.visibility = View.GONE
		}
	}
}