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
import io.reactivex.Single
import java.util.ArrayList
import okhttp3.ResponseBody

interface CompaniesRxRepository : AnalyticsContract {
	fun recentSearches(): Single<List<SearchHistoryItem>>
	fun favourites(): Single<List<SearchHistoryItem>>

	//Companies House API
	fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult>

	fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem>
	fun getCompany(companyNumber: String): Single<Company>
	fun getFilingHistory(companyNumber: String, category: Category, startItem: String): Single<FilingHistory>
	fun fetchCharges(companyNumber: String, startItem: String): Single<Charges>
	fun getInsolvency(companyNumber: String): Single<Insolvency>
	fun getOfficers(companyNumber: String, startItem: String): Single<OfficersResponse>
	fun getOfficerAppointments(officerId: String, startItem: String): Single<AppointmentsResponse>
	fun getPersons(companyNumber: String, startItem: String): Single<PersonsResponse>
	fun getCorporatePerson(companyNumber: String, pscId: String): Single<Person>
	fun getLegalPerson(companyNumber: String, pscId: String): Single<Person>
	fun getDocument(documentId: String): Single<ResponseBody>
	fun writeDocumentPdf(responseBody: ResponseBody, uri: Uri):Single<Uri>

	//Preferences
	fun clearAllRecentSearches()

	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun removeFavourite(favouriteToRemove: SearchHistoryItem)
}
