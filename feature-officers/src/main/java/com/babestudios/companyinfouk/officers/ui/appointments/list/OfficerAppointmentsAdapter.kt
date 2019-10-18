package com.babestudios.companyinfouk.officers.ui.appointments.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment

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
		fun holder(type: Int, view: View): BaseViewHolder<*>
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<AbstractOfficerAppointmentsVisitable> {
		val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
		val v = officerAppointmentsTypeFactory.holder(viewType, view) as BaseViewHolder<AbstractOfficerAppointmentsVisitable>
		RxView.clicks(view)
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