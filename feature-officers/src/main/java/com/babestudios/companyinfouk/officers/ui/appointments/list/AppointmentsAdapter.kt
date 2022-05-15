package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers.databinding.RowOfficerAppointmentsBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

class AppointmentsAdapter(
	private var appointments: List<Appointment>
) : RecyclerView.Adapter<AppointmentsViewHolder>() {

	override fun getItemCount(): Int {
		return appointments.size
	}

	private val itemClicksChannel: Channel<Appointment> = Channel(Channel.UNLIMITED)
	val itemClicks: Flow<Appointment> = itemClicksChannel.consumeAsFlow()

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
		: AppointmentsViewHolder {
		val binding = RowOfficerAppointmentsBinding.inflate(
			LayoutInflater.from(parent.context),
			parent,
			false
		)
		return AppointmentsViewHolder(binding)
	}

	override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
		holder.bind(appointments[position])
		holder.rawBinding.root.clicks().onEach {
			itemClicksChannel.trySend(appointments[position])
		}
	}

	fun updateItems(visitables: List<Appointment>) {
		this.appointments = visitables
		notifyDataSetChanged()
	}
}
