package com.babestudios.companyinfouk.data

import android.net.Uri
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import java.util.*
import javax.inject.Singleton


@Singleton
class AndroidTestCompaniesRepository : CompaniesRepositoryContract {
	override val authorization: String
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
	override val recentSearches: List<SearchHistoryItem>
		get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

	override fun sicLookup(code: String): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun accountTypeLookup(accountType: String): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun filingHistoryLookup(filingHistory: String): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getCompany(companyNumber: String): Single<Company> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getFilingHistory(companyNumber: String, category: String, startItem: String): Single<FilingHistoryList> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun fetchCharges(companyNumber: String, startItem: String): Single<Charges> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getInsolvency(companyNumber: String): Single<Insolvency> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getOfficers(companyNumber: String, startItem: String): Single<Officers> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getOfficerAppointments(officerId: String, startItem: String): Single<Appointments> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getPersons(companyNumber: String, startItem: String): Single<Persons> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun getDocument(documentId: String): Single<ResponseBody> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun writeDocumentPdf(responseBody: ResponseBody): Uri {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun clearAllRecentSearches() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override val favourites: Array<SearchHistoryItem>
		get() {
			val searchHistoryItem = SearchHistoryItem("Acme Painting", "12345678", 12)
			return arrayOf(searchHistoryItem)
		}
}