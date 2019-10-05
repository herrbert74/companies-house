package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*
import java.util.*

interface CompaniesRepositoryContract {
	val authorization: String
	val recentSearches: List<SearchHistoryItem>
	val favourites: Array<SearchHistoryItem>

	//String mapping
	fun sicLookup(code: String): String

	fun accountTypeLookup(accountType: String): String
	fun filingHistoryLookup(filingHistory: String): String

	//Companies House API
	fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult>

	fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem>
	fun getCompany(companyNumber: String): Single<Company>
	fun getFilingHistory(companyNumber: String, category: String, startItem: String): Single<FilingHistoryList>
	fun fetchCharges(companyNumber: String, startItem: String): Single<Charges>
	fun getInsolvency(companyNumber: String): Single<Insolvency>
	fun getOfficers(companyNumber: String, startItem: String): Single<Officers>
	fun getOfficerAppointments(officerId: String, startItem: String): Single<Appointments>
	fun getPersons(companyNumber: String, startItem: String): Single<Persons>
	fun getDocument(documentId: String): Single<ResponseBody>
	fun writeDocumentPdf(responseBody: ResponseBody): Uri

	//Preferences
	fun clearAllRecentSearches()

	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun removeFavourite(favouriteToRemove: SearchHistoryItem)
}

open class CompaniesRepository constructor(
		private val context: Context,
		private val companiesHouseService: CompaniesHouseService,
		private val companiesHouseDocumentService: CompaniesHouseDocumentService,
		private var preferencesHelper: PreferencesHelper,
		base64Wrapper: Base64Wrapper,
		private val constantsHelper: ConstantsHelper,
		private val filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper
) : CompaniesRepositoryContract {

	override val authorization: String = "Basic " + base64Wrapper.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.toByteArray(), Base64.NO_WRAP)

	override val recentSearches: List<SearchHistoryItem>
		get() = preferencesHelper.recentSearches

	override val favourites: Array<SearchHistoryItem>
		get() = preferencesHelper.favourites

	override fun sicLookup(code: String): String {
		return constantsHelper.sicLookUp(code)
	}

	override fun accountTypeLookup(accountType: String): String {
		return constantsHelper.accountTypeLookUp(accountType)
	}

	override fun filingHistoryLookup(filingHistory: String): String {
		return filingHistoryDescriptionsHelper.filingHistoryLookUp(filingHistory)
	}

	override fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult> {
		return companiesHouseService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		return preferencesHelper.addRecentSearch(searchHistoryItem)
	}

	override fun getCompany(companyNumber: String): Single<Company> {
		return companiesHouseService.getCompany(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getFilingHistory(companyNumber: String, category: String, startItem: String): Single<FilingHistoryList> {
		return companiesHouseService.getFilingHistory(authorization, companyNumber, category, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun fetchCharges(companyNumber: String, startItem: String): Single<Charges> {
		return companiesHouseService.getCharges(authorization, companyNumber, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getInsolvency(companyNumber: String): Single<Insolvency> {
		return companiesHouseService.getInsolvency(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getOfficers(companyNumber: String, startItem: String): Single<Officers> {
		return companiesHouseService.getOfficers(authorization, companyNumber, null, null, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getOfficerAppointments(officerId: String, startItem: String): Single<Appointments> {
		return companiesHouseService.getOfficerAppointments(authorization, officerId, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getPersons(companyNumber: String, startItem: String): Single<Persons> {
		return companiesHouseService.getPersons(authorization, companyNumber, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun getDocument(documentId: String): Single<ResponseBody> {
		return companiesHouseDocumentService.getDocument(authorization, "application/pdf", documentId)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	override fun writeDocumentPdf(responseBody: ResponseBody): Uri {
		val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
		val pdfFile = File(dir, "doc.pdf")
		var inputStream: InputStream? = null
		var outputStream: OutputStream? = null
		try {
			val fileReader = ByteArray(4096)
			try {
				inputStream = responseBody.byteStream()
				outputStream = FileOutputStream(pdfFile)
				while (true) {
					val read = inputStream!!.read(fileReader)
					if (read == -1) {
						break
					}
					outputStream.write(fileReader, 0, read)
				}
				outputStream.flush()
			} catch (e: IOException) {
				Log.d("test", e.localizedMessage)
			} finally {
				inputStream?.close()

				outputStream?.close()
			}
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		} catch (e: IOException) {
			Log.d("test", "Error during closing input stream" + e.localizedMessage)
		}

		return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", pdfFile)
	}

	override fun clearAllRecentSearches() {
		preferencesHelper.clearAllRecentSearches()
	}

	override fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return preferencesHelper.addFavourite(searchHistoryItem)
	}

	override fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		val items = preferencesHelper.favourites
		return if (items.isNotEmpty()) {
			val favourites = ArrayList(listOf(*items))
			favourites.contains(searchHistoryItem)
		} else {
			false
		}
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		preferencesHelper.removeFavourite(favouriteToRemove)
	}
}
