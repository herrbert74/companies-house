package com.babestudios.companyinfouk.officers.ui.appointments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment
import com.babestudios.companyinfouk.officers.databinding.RowOfficerAppointmentsBinding
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class OfficerAppointmentsAdapter(private var officerAppointmentsVisitables: List<AbstractOfficerAppointmentsVisitable>
								 , private val officerAppointmentsTypeFactory: OfficerAppointmentsTypeFactory)
	: RecyclerView.Adapter<BaseViewHolder<AbstractOfficerAppointmentsVisitable>>() {

	override fun getItemCount(): Int {
		return officerAppointmentsVisitables.size
	}

	override fun getItemViewType(position: Int): Int {
		return officerAppointmentsVisitables[position].type(officerAppointmentsTypeFactory)
	}

	private val itemClickSubject = PublishSubject.create<BaseViewHolder<AbstractOfficerAppointmentsVisitable>>()

	fun getViewClickedObservable(): Observable<BaseViewHolder<AbstractOfficerAppointmentsVisitable>> {
		return itemClickSubject
	}

	interface OfficerAppointmentsTypeFactory {
		fun type(appointment: Appointment): Int
		fun holder(type: Int, binding: ViewBinding): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
			: BaseViewHolder<AbstractOfficerAppointmentsVisitable> {
		val binding = RowOfficerAppointmentsBinding.inflate(
				LayoutInflater.from(parent.context),
				parent,
				false)
		val v = officerAppointmentsTypeFactory.holder(viewType, binding)
				as BaseViewHolder<AbstractOfficerAppointmentsVisitable>
		RxView.clicks(binding.root)
				.takeUntil(RxView.detaches(parent))
				.map { v }
				.subscribe(itemClickSubject)
		return v
	}

	override fun onBindViewHolder(holder: BaseViewHolder<AbstractOfficerAppointmentsVisitable>, position: Int) {
		holder.bind(officerAppointmentsVisitables[position])
	}

	fun updateItems(visitables: List<AbstractOfficerAppointmentsVisitable>) {
		officerAppointmentsVisitables = visitables
		notifyDataSetChanged()
	}
}
