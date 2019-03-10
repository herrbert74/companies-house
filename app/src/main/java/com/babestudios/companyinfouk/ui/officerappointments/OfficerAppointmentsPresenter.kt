package com.babestudios.companyinfouk.ui.officerappointments

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ObserverWrapper
import com.babestudios.companyinfouk.BuildConfig
import com.uber.autodispose.AutoDispose
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.ui.officerappointments.list.AbstractOfficerAppointmentsVisitable
import com.babestudios.companyinfouk.ui.officerappointments.list.OfficerAppointmentsVisitable
import io.reactivex.CompletableSource
import javax.inject.Inject

interface OfficerAppointmentsPresenterContract : Presenter<OfficerAppointmentsState, OfficerAppointmentsViewModel> {
	fun fetchAppointments(officerId: String)
	fun loadMoreAppointments(page: Int)
}

@SuppressLint("CheckResult")
class OfficerAppointmentsPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<OfficerAppointmentsState, OfficerAppointmentsViewModel>(), OfficerAppointmentsPresenterContract {

	override fun setViewModel(viewModel: OfficerAppointmentsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.appointmentItems?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.APPOINTMENTS_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.officerId?.also {
				fetchAppointments(it)
			}
		}
	}

	override fun fetchAppointments(officerId: String) {
		companiesRepository.getOfficerAppointments(officerId, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : ObserverWrapper<Appointments>(this) {
					override fun onSuccess(reply: Appointments) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.officerName = reply.name
								this.contentChange = ContentChange.APPOINTMENTS_RECEIVED
								this.appointmentItems = convertToVisitables(reply)
								this.totalResults = reply.totalResults?.toInt()
							}
						}
					}
				})
	}

	override fun loadMoreAppointments(page: Int) {
		if (viewModel.state.value?.appointmentItems == null || viewModel.state.value?.appointmentItems!!.size < viewModel.state.value?.totalResults!!) {
			companiesRepository.getOfficerAppointments(viewModel.state.value?.officerId
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : ObserverWrapper<Appointments>(this) {
						override fun onSuccess(reply: Appointments) {
							val newList = viewModel.state.value?.appointmentItems?.toMutableList()
							newList?.addAll(convertToVisitables(reply))
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.APPOINTMENTS_RECEIVED
									newList?.toList()?.let { list -> this.appointmentItems = list }
								}
							}
						}
					})
		}
	}

	private fun convertToVisitables(reply: Appointments): List<AbstractOfficerAppointmentsVisitable> {
		return reply.items?.let {
			ArrayList(it.toMutableList().map { item -> OfficerAppointmentsVisitable(item) })
		} ?: emptyList()
	}

}