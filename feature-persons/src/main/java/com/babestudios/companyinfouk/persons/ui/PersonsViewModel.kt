package com.babestudios.companyinfouk.persons.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.appendAt
import com.babestudios.base.annotation.Mockable
import com.babestudios.base.mvrx.BaseViewModel
import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsVisitableBase
import com.babestudios.companyinfouk.persons.ui.persons.list.PersonsVisitable

@Mockable
class PersonsViewModel(
		personsState: PersonsState,
		private val companiesRepository: CompaniesRepositoryContract,
		val personsNavigator: PersonsNavigator
) : BaseViewModel<PersonsState>(personsState, companiesRepository) {

	companion object : MvRxViewModelFactory<PersonsViewModel, PersonsState> {

		@JvmStatic
		override fun create(viewModelContext: ViewModelContext, state: PersonsState): PersonsViewModel? {
			val companiesRepository = viewModelContext.activity<PersonsActivity>().injectCompaniesHouseRepository()
			val personsNavigator = viewModelContext.activity<PersonsActivity>().injectPersonsNavigator()
			return PersonsViewModel(
					state,
					companiesRepository,
					personsNavigator
			)
		}

		override fun initialState(viewModelContext: ViewModelContext): PersonsState? {
			val companyNumber = viewModelContext.activity<PersonsActivity>().provideCompanyNumber()
			return if (companyNumber.isNotEmpty())
				PersonsState(companyNumber = companyNumber)
			else
				null
		}
	}

	//region persons

	fun fetchPersons(companyNumber: String) {
		companiesRepository.getPersons(companyNumber, "0")
				.execute {
					copy(
							personsRequest = it,
							persons = convertToVisitables(it()),
							totalPersonCount = it()?.totalResults ?: 0
					)
				}
	}


	fun loadMorePersons(page: Int) {
		withState { state ->
			if (state.persons.size < state.totalPersonCount) {
				companiesRepository.getPersons(
						state.companyNumber
								?: "",
						(page * Integer
								.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))
								.toString()
				).execute {
					copy(
							personsRequest = it,
							persons = persons.appendAt(convertToVisitables(it()), persons.size + 1),
							totalPersonCount = it()?.totalResults ?: 0
					)
				}
			}
		}
	}

	private fun convertToVisitables(reply: Persons?): List<PersonsVisitableBase> {
		return ArrayList(reply?.items?.map { item -> PersonsVisitable(item) } ?: emptyList())
	}

	fun personItemClicked(adapterPosition: Int) {
		withState { state ->
			setState {
				copy(
						personItem = (state.persons[adapterPosition] as PersonsVisitable).person
				)
			}
		}
		personsNavigator.personsToPersonDetails()
	}

	//endregion
}
