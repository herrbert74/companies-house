package com.babestudios.companyinfouk.ui.officerappointments

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments

internal class OfficerAppointmentsAdapter(c: Context, private val appointments: Appointments) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

	private val mItemListener: AppointmentsRecyclerViewClickListener

	init {
		mItemListener = c as AppointmentsRecyclerViewClickListener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val itemLayoutView: View
		if (viewType == TYPE_APPOINTMENT) {
			itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.row_officer_appointments, parent, false)
			return TransactionsViewHolder(itemLayoutView)
		} else if (viewType == TYPE_HEADER) {
			itemLayoutView = LayoutInflater.from(parent.context)
					.inflate(R.layout.row_officer_appointments_header, parent, false)

			return HeaderViewHolder(itemLayoutView)
		}
		throw RuntimeException("There is no type that matches the type $viewType + make sure your using types correctly")
	}

	override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
		if (viewHolder is HeaderViewHolder) {
			viewHolder.textViewOfficerName?.text = appointments.name
		} else if (viewHolder is TransactionsViewHolder) {
			appointments.items?.let {
				viewHolder.textViewAppointedOn?.text = it[position - 1].appointedOn
				viewHolder.textViewCompanyName?.text = it[position - 1].appointedTo?.companyName
				viewHolder.textViewCompanyStatus?.text = it[position - 1].appointedTo?.companyStatus
				viewHolder.textViewRole?.text = it[position - 1].officerRole
				if (it[position - 1].resignedOn != null) {
					viewHolder.textViewResignedOn?.text = it[position - 1].resignedOn
				} else {
					viewHolder.textViewResignedOn?.visibility = View.GONE
					viewHolder.textViewLabelResignedOn?.visibility = View.GONE
				}
			} ?: run {
				viewHolder.textViewResignedOn?.visibility = View.GONE
				viewHolder.textViewLabelResignedOn?.visibility = View.GONE
			}
		}

	}

	override fun getItemViewType(position: Int): Int {
		return if (isPositionHeader(position)) {
			TYPE_HEADER
		} else {
			TYPE_APPOINTMENT
		}
	}

	private fun isPositionHeader(position: Int): Boolean {
		return position == 0
	}

	override fun getItemId(position: Int): Long {
		return position.toLong()
	}

	override fun getItemCount(): Int {
		return appointments.items?.let { it.size + 1 } ?: 1
	}

	inner class HeaderViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
		@JvmField
		@BindView(R.id.textViewOfficerName)
		internal var textViewOfficerName: TextView? = null

		init {
			ButterKnife.bind(this, itemView)

		}
	}

	inner class TransactionsViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

		@JvmField
		@BindView(R.id.textViewAppointedOn)
		internal var textViewAppointedOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewCompanyName)
		internal var textViewCompanyName: TextView? = null
		@JvmField
		@BindView(R.id.textViewCompanyStatus)
		internal var textViewCompanyStatus: TextView? = null
		@JvmField
		@BindView(R.id.textViewRole)
		internal var textViewRole: TextView? = null
		@JvmField
		@BindView(R.id.textViewLabelResignedOn)
		internal var textViewLabelResignedOn: TextView? = null
		@JvmField
		@BindView(R.id.textViewResignedOn)
		internal var textViewResignedOn: TextView? = null

		init {
			ButterKnife.bind(this, itemView)
			itemView.setOnClickListener(this)
		}

		override fun onClick(v: View) {
			appointments.items?.let {
				mItemListener.appointmentItemClicked(v, this.layoutPosition, it[layoutPosition - 1])
			}
		}
	}

	internal interface AppointmentsRecyclerViewClickListener {
		fun appointmentItemClicked(v: View, position: Int, item: Appointment)
	}

	companion object {

		private const val TYPE_HEADER = 0
		private const val TYPE_APPOINTMENT = 1
	}
}
