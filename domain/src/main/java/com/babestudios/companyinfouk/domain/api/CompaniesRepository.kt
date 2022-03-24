package com.babestudios.companyinfouk.domain.api

import android.net.Uri
import com.babestudios.base.data.AnalyticsContract
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.domain.model.filinghistory.Category
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.model.persons.PersonsResponse
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody

interface CompaniesRepository : AnalyticsContract {

	//Companies House API
	suspend fun searchCompanies(queryText: CharSequence, startItem: String): CompanySearchResult
	suspend fun getCompany(companyNumber: String): Company
	suspend fun getFilingHistory(companyNumber: String, category: Category, startItem: String): FilingHistory
	suspend fun fetchCharges(companyNumber: String, startItem: String): Charges
	suspend fun getInsolvency(companyNumber: String): Insolvency
	suspend fun getOfficers(companyNumber: String, startItem: String): OfficersResponse
	suspend fun getOfficerAppointments(officerId: String, startItem: String): AppointmentsResponse
	suspend fun getPersons(companyNumber: String, startItem: String): PersonsResponse
	suspend fun getCorporatePerson(companyNumber: String, pscId: String): Person
	suspend fun getLegalPerson(companyNumber: String, pscId: String): Person

	//Companies House Documents API
	suspend fun getDocument(documentId: String): ResponseBody
	suspend fun writeDocumentPdf(responseBody: ResponseBody, uri: Uri): Uri

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
