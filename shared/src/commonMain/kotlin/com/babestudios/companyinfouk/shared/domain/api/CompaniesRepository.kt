package com.babestudios.companyinfouk.shared.domain.api

import com.babestudios.base.kotlin.api.AnalyticsContract
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.common.ApiResult
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

@Suppress("TooManyFunctions")
interface CompaniesRepository : AnalyticsContract {

	//Companies House API
	suspend fun searchCompanies(queryText: CharSequence, startItem: String): CompanySearchResult
	suspend fun getCompany(companyNumber: String): Company
	suspend fun getFilingHistory(companyNumber: String, category: Category, startItem: String): ApiResult<FilingHistory>
	suspend fun getCharges(companyNumber: String, startItem: String): ApiResult<Charges>
	suspend fun getInsolvency(companyNumber: String): ApiResult<Insolvency>
	suspend fun getOfficers(companyNumber: String, startItem: String): ApiResult<OfficersResponse>
	suspend fun getOfficerAppointments(officerId: String, startItem: String): ApiResult<AppointmentsResponse>
	suspend fun getPersons(companyNumber: String, startItem: String): ApiResult<PersonsResponse>
	suspend fun getCorporatePerson(companyNumber: String, pscId: String): Person
	suspend fun getLegalPerson(companyNumber: String, pscId: String): Person

	//Preferences - Search
	suspend fun recentSearches(): List<SearchHistoryItem>
	fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem>
	fun clearAllRecentSearches()

	//Preferences - Favourites
	suspend fun favourites(): List<SearchHistoryItem>
	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun removeFavourite(favouriteToRemove: SearchHistoryItem)
}
