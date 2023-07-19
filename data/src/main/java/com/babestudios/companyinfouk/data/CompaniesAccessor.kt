package com.babestudios.companyinfouk.data

import android.os.Bundle
import com.babestudios.base.data.network.OfflineException
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategory
import com.babestudios.companyinfouk.data.mappers.toAppointmentsResponse
import com.babestudios.companyinfouk.data.mappers.toCharges
import com.babestudios.companyinfouk.data.mappers.toCompany
import com.babestudios.companyinfouk.data.mappers.toFilingHistory
import com.babestudios.companyinfouk.data.mappers.toInsolvency
import com.babestudios.companyinfouk.data.mappers.toOfficersResponse
import com.babestudios.companyinfouk.data.mappers.toPerson
import com.babestudios.companyinfouk.data.mappers.toPersonsResponse
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
import com.babestudios.companyinfouk.shared.domain.model.common.apiRunCatching
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.github.michaelbull.result.mapError
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
//import org.lighthousegames.logging.logging

internal const val COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE = "50"

internal class CompaniesAccessor constructor(
	private val companiesHouseApi: CompaniesHouseApi,
	private var preferencesHelper: PreferencesHelper,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val ioContext: CoroutineDispatcher,
) : CompaniesRepository {

	//private val log = logging()

	override suspend fun recentSearches(): List<SearchHistoryItem> {
		return preferencesHelper.recentSearches
	}

	override suspend fun favourites(): List<SearchHistoryItem> {
		return preferencesHelper.favourites.toList()
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
		return preferencesHelper.addRecentSearch(searchHistoryItem)
	}

	override suspend fun getCompany(companyNumber: String): Company {
		return withContext(ioContext) {
			val companyDto = companiesHouseApi.getCompany(companyNumber)
			companyDto.toCompany()
		}
	}

	override suspend fun getFilingHistory(
		companyNumber: String,
		category: com.babestudios.companyinfouk.shared.domain.model.filinghistory.Category,
		startItem: String,
	): ApiResult<FilingHistory> {
		return apiRunCatching {
			withContext(ioContext) {
				val historyDto = companiesHouseApi.getFilingHistory(
					companyNumber,
					mapFilingHistoryCategory(category).getSerialName(),
					COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				historyDto.toFilingHistory()
			}
		}.mapError { OfflineException(FilingHistory()) }
	}

	//TODO Extract to base-data
	private fun Enum<*>.getSerialName(): String {
		return try {
			val f = this.javaClass.getField(this.name)
			val a = f.getAnnotation(SerialName::class.java)
			a?.value ?: ""
		} catch (ignored: Throwable) {
			""
		}

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
		preferencesHelper.clearAllRecentSearches()
	}

	override fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return preferencesHelper.addFavourite(searchHistoryItem)
	}

	override fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return preferencesHelper.favourites.contains(searchHistoryItem)
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		preferencesHelper.removeFavourite(favouriteToRemove)
	}

//region analytics

	override fun logAppOpen() {
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)
	}

	override fun logScreenView(screenName: String) {
		val bundle = Bundle()
		bundle.putString("screen_name", screenName)
		firebaseAnalytics.logEvent("screenView", bundle)
	}

	override fun logSearch(queryText: String) {
		val bundle = Bundle()
		bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, queryText)
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
	}

//endregion

}
