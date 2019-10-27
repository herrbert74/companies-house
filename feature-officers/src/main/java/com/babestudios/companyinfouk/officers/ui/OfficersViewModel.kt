package com.babestudios.companyinfouk.officers.ui

import androidx.navigation.Navigator
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.base.mvrx.ErrorType
import com.babestudios.base.mvrx.ScreenState
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SingleObserverWrapperVM
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.ui.appointments.list.AbstractOfficerAppointmentsVisitable
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.AbstractOfficersVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersVisitable
import java.util.regex.Pattern

@Suppress("CheckResult")
class OfficersViewModel(
		officersState: OfficersState,
		private val companiesRepository: CompaniesRepositoryContract,
		private val officersNavigator: OfficersNavigator,
		private val errorResolver: ErrorResolver
) : BaseViewModel<OfficersState>(officersState) {

	companion object : MvRxViewModelFactory<OfficersViewModel, OfficersState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: OfficersState): OfficersViewModel? {
			val companiesRepository = viewModelContext.activity<OfficersActivity>().injectCompaniesHouseRepository()
			val officersNavigator = viewModelContext.activity<OfficersActivity>().injectOfficersNavigator()
			val errorResolver = viewModelContext.activity<OfficersActivity>().injectErrorResolver()
			return OfficersViewModel(
					state,
					companiesRepository,
					officersNavigator,
					errorResolver
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): OfficersState? {
			val companyNumber = viewModelContext.activity<OfficersActivity>().provideCompanyNumber()
			return OfficersState(companyNumber = companyNumber)
		}
	}

	//region officers

	fun fetchOfficers(companyNumber: String) {
		companiesRepository.getOfficers(companyNumber, "0")
				.subscribeWith(object : SingleObserverWrapperVM<Officers>(errorHandler) {

					override fun onSuccess(reply: Officers) {
						setState {
							copy(
									officersScreenState = ScreenState.Complete,
									officerItems = convertToVisitables(reply),
									totalOfficersCount = reply.totalResults
							)
						}
					}
				})
	}


	fun loadMoreOfficers(page: Int) {
		withState {
			if (it.officerItems.size < it.totalOfficersCount) {
				companiesRepository.getOfficers(it.companyNumber, (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
						.subscribeWith(object : SingleObserverWrapperVM<Officers>(errorHandler) {

							override fun onSuccess(reply: Officers) {
								val newList = it.officerItems.toMutableList()
								newList.addAll(convertToVisitables(reply))
								setState {
									copy(
											officersScreenState = ScreenState.Complete,
											officerItems = newList.toList()
									)
								}
							}
						})
			}
		}
	}

	private fun convertToVisitables(reply: Officers): List<AbstractOfficersVisitable> {
		return ArrayList(reply.items.map { item -> OfficersVisitable(item) })
	}

	fun officerItemClicked(adapterPosition: Int) {
		withState { state ->

			val newOfficerItem = (state.officerItems[adapterPosition] as OfficersVisitable).officerItem
			val pattern = Pattern.compile("officers/(.+)/appointments")
			val matcher = pattern.matcher(newOfficerItem.links?.officer?.appointments ?: "")
			var officerId = ""
			if (matcher.find()) {
				officerId = matcher.group(1) ?: ""
			}

			setState {
				copy(
						officerItem = newOfficerItem,
						officerId = officerId,
						officerDetailsScreenState = ScreenState.Complete
				)
			}
		}
		officersNavigator.officersToOfficerDetails()
	}

	//endregion

	//region officer details

	fun officerAppointmentsClicked(extras: Navigator.Extras) {
		officersNavigator.officersDetailsToAppointments(extras)
	}

	//endregion

	//region officer appointments

	fun fetchAppointments() {
		setState {
			copy(
					officerAppointmentsScreenState = ScreenState.Loading
			)
		}
		withState {
			companiesRepository.getOfficerAppointments(it.officerId, "0")
					//.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
					.subscribeWith(object : SingleObserverWrapperVM<Appointments>(errorHandler) {

						override fun onSuccess(reply: Appointments) {
							setState {
								copy(
										officerName = reply.name ?: "",
										officerAppointmentsScreenState = ScreenState.Complete,
										appointmentItems = convertToVisitables(reply),
										totalAppointmentsCount = reply.totalResults?.toInt() ?: 0
								)
							}
						}

					})
		}
	}

	fun loadMoreAppointments(page: Int) {
		withState { state ->
			if (state.appointmentItems.size < state.totalAppointmentsCount) {
				companiesRepository.getOfficerAppointments(
						state.officerId,
						(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
						.subscribeWith(object : SingleObserverWrapperVM<Appointments>(errorHandler) {

							override fun onSuccess(reply: Appointments) {
								val newList = state.appointmentItems.toMutableList()
								newList.addAll(convertToVisitables(reply))
								setState {
									copy(
											officerAppointmentsScreenState = ScreenState.Complete,
											appointmentItems = newList
									)
								}
							}
						})
			}
		}
	}

	private fun convertToVisitables(reply: Appointments): List<AbstractOfficerAppointmentsVisitable> {
		return reply.items?.let {
			ArrayList(it.toMutableList().map { item -> OfficerAppointmentsVisitable(item) })
		} ?: emptyList()
	}

	//endregion

	override var errorHandler = fun(errorType: ErrorType, errorMessage: String) {
		val resolvedErrorMessage = errorResolver.getErrorMessageFromResponseBody(errorMessage)
		errorType.message = resolvedErrorMessage ?: ""
		setState {
			copy(
					officersScreenState = ScreenState.Error(errorType),
					totalOfficersCount = 0
			)
		}
	}
}