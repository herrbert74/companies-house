package com.babestudios.companyinfouk.shared.data

import com.babestudios.base.data.network.OfflineException
import com.babestudios.companyinfouk.shared.AnalyticsFactory
import com.babestudios.companyinfouk.shared.data.local.Prefs
import com.babestudios.companyinfouk.shared.data.mapper.toAppointmentsResponse
import com.babestudios.companyinfouk.shared.data.mapper.toCharges
import com.babestudios.companyinfouk.shared.data.mapper.toCompany
import com.babestudios.companyinfouk.shared.data.mapper.toFilingHistory
import com.babestudios.companyinfouk.shared.data.mapper.toInsolvency
import com.babestudios.companyinfouk.shared.data.mapper.toOfficersResponse
import com.babestudios.companyinfouk.shared.data.mapper.toPerson
import com.babestudios.companyinfouk.shared.data.mapper.toPersonsResponse
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.common.apiRunCatching
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

// import org.lighthousegames.logging.logging

internal const val COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE = "50"

@Suppress("TooManyFunctions")
internal class CompaniesAccessor(
	private val companiesHouseApi: CompaniesHouseApi,
	private var prefsAccessor: Prefs,
	analyticsFactory: AnalyticsFactory,
	private val ioContext: CoroutineDispatcher,
) : CompaniesRepository {

	private val firebaseAnalytics = analyticsFactory.createAnalytics()
	// private val log = logging()

	override suspend fun recentSearches(): List<SearchHistoryItem> {
		return prefsAccessor.recentSearches
	}

	override suspend fun favourites(): List<SearchHistoryItem> {
		return prefsAccessor.favourites.toList()
	}

	override suspend fun searchCompanies(queryText: CharSequence, startItem: String): CompanySearchResult {
		return companiesHouseApi
			.searchCompanies(
				queryText.toString(),
				COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
	}

	override fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		return prefsAccessor.addRecentSearch(searchHistoryItem)
	}

	override suspend fun getCompany(companyNumber: String): Company {
		return withContext(ioContext) {
			val companyDto = companiesHouseApi.getCompany(companyNumber)
			companyDto.toCompany()
		}
	}

	override suspend fun getFilingHistory(
		companyNumber: String,
		category: Category,
		startItem: String,
	): ApiResult<FilingHistory> {
		return apiRunCatching {
			withContext(ioContext) {
				val historyDto = companiesHouseApi.getFilingHistory(
					companyNumber,
					category.serialName,
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				historyDto.toFilingHistory()
			}
		}.mapError { OfflineException(FilingHistory()) }
	}

	override suspend fun getCharges(companyNumber: String, startItem: String): ApiResult<Charges> {
		return apiRunCatching {
			withContext(ioContext) {
				val chargesDto = companiesHouseApi.getCharges(
					companyNumber,
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				chargesDto.toCharges()
			}
		}.mapError { OfflineException(Charges()) }
	}

	override suspend fun getInsolvency(companyNumber: String): ApiResult<Insolvency> {
		return apiRunCatching {
			withContext(ioContext) {
				val insolvencyDto = companiesHouseApi.getInsolvency(companyNumber)
				insolvencyDto.toInsolvency()
			}
		}.mapError { OfflineException(Insolvency()) }
	}

	override suspend fun getOfficers(companyNumber: String, startItem: String): ApiResult<OfficersResponse> {
		return apiRunCatching {
			withContext(ioContext) {
				val officersResponseDto = companiesHouseApi.getOfficers(
					companyNumber,
					null,
					null,
					null,
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				officersResponseDto.toOfficersResponse()
			}
		}.mapError {
			OfflineException(
				OfficersResponse()
			)
		}

	}

	override suspend fun getOfficerAppointments(officerId: String, startItem: String): ApiResult<AppointmentsResponse> {
		return apiRunCatching {
			withContext(ioContext) {
				val appointmentsResponseDto = companiesHouseApi.getOfficerAppointments(
					officerId,
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				appointmentsResponseDto.toAppointmentsResponse()
			}
		}.mapError { OfflineException(AppointmentsResponse()) }
	}

	override suspend fun getPersons(companyNumber: String, startItem: String): ApiResult<PersonsResponse> {
		return apiRunCatching {
			withContext(ioContext) {
				val personsResponseDto = companiesHouseApi.getPersons(
					companyNumber,
					null,
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				personsResponseDto.toPersonsResponse()
			}
		}.mapError { OfflineException(PersonsResponse()) }
	}

	override suspend fun getCorporatePerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseApi.getCorporatePerson(companyNumber, pscId)
			personDto.toPerson()
		}
	}

	override suspend fun getLegalPerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseApi.getLegalPerson(companyNumber, pscId)
			personDto.toPerson()
		}
	}

	override fun clearAllRecentSearches() {
		prefsAccessor.clearAllRecentSearches()
	}

	override fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return prefsAccessor.addFavourite(searchHistoryItem)
	}

	override fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return prefsAccessor.favourites.contains(searchHistoryItem)
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		prefsAccessor.removeFavourite(favouriteToRemove)
	}

//region analytics

	override fun logAppOpen() {
		firebaseAnalytics.logAppOpen()
	}

	override fun logScreenView(screenName: String) {
		firebaseAnalytics.logScreenView(screenName)
	}

	override fun logSearch(queryText: String) {
		firebaseAnalytics.logSearch(queryText)
	}

//endregion

}
