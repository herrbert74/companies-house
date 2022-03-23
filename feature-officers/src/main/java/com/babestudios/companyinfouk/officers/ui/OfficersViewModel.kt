package com.babestudios.companyinfouk.officers.ui

import androidx.navigation.Navigator
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsVisitableBase
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsVisitable
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersVisitableBase
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersVisitable
import java.util.regex.Pattern

class OfficersViewModel(
	officersState: OfficersState,
	private val companiesRepository: CompaniesRxRepository,
	var officersNavigator: OfficersNavigator
) : BaseViewModel<OfficersState>(officersState, companiesRepository) {

	companion object : MvRxViewModelFactory<OfficersViewModel, OfficersState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: OfficersState): OfficersViewModel? {
			val companiesRepository = viewModelContext.activity<OfficersActivity>().injectCompaniesHouseRepository()
			val officersNavigator = viewModelContext.activity<OfficersActivity>().injectOfficersNavigator()
			return OfficersViewModel(
					state,
					companiesRepository,
					officersNavigator
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): OfficersState? {
			val companyNumber = viewModelContext.activity<OfficersActivity>().provideCompanyNumber()
			return if (companyNumber.isNotEmpty())
				OfficersState(companyNumber = companyNumber)
			else
				null
		}
	}

	fun setNavigator(navigator: OfficersNavigator) {
		officersNavigator = navigator
	}

	//region officers

	fun fetchOfficers(companyNumber: String) {
		companiesRepository.getOfficers(companyNumber, "0")
				.execute {
					copy(
							officersRequest = it,
							officerItems = convertToVisitables(it()),
							totalOfficersCount = it()?.totalResults ?: 0
					)
				}
	}


	fun loadMoreOfficers(page: Int) {
		withState { state ->
			if (state.officerItems.size < state.totalOfficersCount) {
				companiesRepository.getOfficers(
						state.companyNumber,
						(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				).execute {
					copy(
							officersRequest = it,
							officerItems = officerItems.appendAt(
									convertToVisitables(it()),
									officerItems.size + 1
							),
							totalOfficersCount = it()?.totalResults ?: 0
					)
				}
			}
		}
	}

	private fun convertToVisitables(reply: OfficersResponse?): List<OfficersVisitableBase> {
		return ArrayList(reply?.items?.map { item -> OfficersVisitable(item) } ?: emptyList())
	}

	fun officerItemClicked(adapterPosition: Int) {
		withState { state ->
			val newOfficerItem = (state.officerItems[adapterPosition] as OfficersVisitable).officerItem
			val pattern = Pattern.compile("officers/(.+)/appointments")
			val matcher = pattern.matcher(newOfficerItem.links.officer?.appointments ?: "")
			var officerId = ""
			if (matcher.find()) {
				officerId = matcher.group(1) ?: ""
			}
			setState {
				copy(
						selectedOfficer = newOfficerItem,
						selectedOfficerId = officerId,
						officerName = newOfficerItem.name
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
		withState { state ->
			companiesRepository.getOfficerAppointments(state.selectedOfficerId, "0")
					.doOnSubscribe { setState { copy(officerAppointmentsRequest = Loading()) } }
					.execute {
						copy(
								officerAppointmentsRequest = it,
								appointmentItems = convertToVisitables(it()),
								totalAppointmentsCount = it()?.totalResults ?: 0
						)
					}
		}
	}

	fun loadMoreAppointments(page: Int) {
		withState { state ->
			if (state.appointmentItems.size < state.totalAppointmentsCount) {
				companiesRepository.getOfficerAppointments(
						state.selectedOfficerId,
						(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString()
				)
						.doOnSubscribe { setState { copy(officerAppointmentsRequest = Loading()) } }
						.execute {
							copy(
									officerAppointmentsRequest = it,
									appointmentItems = appointmentItems.appendAt(
											convertToVisitables(it()),
											appointmentItems.size + 1
									),
									totalAppointmentsCount = it()?.totalResults ?: 0
							)
						}
			}
		}
	}

	private fun convertToVisitables(reply: AppointmentsResponse?): List<OfficerAppointmentsVisitableBase> {
		return reply?.items?.let {
			ArrayList(it.toMutableList().map { item -> OfficerAppointmentsVisitable(item) })
		} ?: emptyList()
	}

	fun showOnMapClicked() {
		withState {
			officersNavigator.officersDetailsToMap(
					it.selectedOfficer?.name ?: "",
					it.selectedOfficer?.address ?: Address()
			)
		}
	}

	//endregion

}
