package com.babestudios.companyinfouk.ui.officerappointments.list

import android.view.View
import com.babestudios.base.mvp.list.BaseViewHolder
import kotlinx.android.synthetic.main.row_officer_appointments.view.*

class OfficerAppointmentsViewHolder(itemView: View) : BaseViewHolder<AbstractOfficerAppointmentsVisitable>(itemView) {
	override fun bind(visitable: AbstractOfficerAppointmentsVisitable) {
		val appointment = (visitable as OfficerAppointmentsVisitable).appointment
		itemView.textViewAppointedOn?.text = appointment.appointedOn
		itemView.textViewCompanyName?.text = appointment.appointedTo?.companyName
		itemView.textViewCompanyStatus?.text = appointment.appointedTo?.companyStatus
		itemView.textViewRole?.text = appointment.officerRole
		if (appointment.resignedOn != null) {
			itemView.textViewResignedOn?.visibility = View.VISIBLE
			itemView.textViewLabelResignedOn?.visibility = View.VISIBLE
			itemView.textViewResignedOn?.text = appointment.resignedOn
		} else {
			itemView.textViewResignedOn?.visibility = View.GONE
			itemView.textViewLabelResignedOn?.visibility = View.GONE
		}
	}
}