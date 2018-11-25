package com.babestudios.companyinfouk.data

import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.util.Base64
import android.util.Log
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.local.ApiLookupHelper
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.utils.Base64Wrapper
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataManager @Inject
constructor(private val companiesHouseService: CompaniesHouseService, private val companiesHouseDocumentService: CompaniesHouseDocumentService, private val preferencesHelper: PreferencesHelper, base64Wrapper: Base64Wrapper) {

	private val authorization: String = "Basic " + base64Wrapper.encodeToString(BuildConfig.COMPANIES_HOUSE_API_KEY.toByteArray(), Base64.NO_WRAP)
	private val apiLookupHelper = ApiLookupHelper()

	val recentSearches: List<SearchHistoryItem>
		get() = preferencesHelper.recentSearches

	val favourites: Array<SearchHistoryItem>
		get() = preferencesHelper.favourites

	fun sicLookup(code: String): String {
		return apiLookupHelper.sicLookup(code)
	}

	fun accountTypeLookup(accountType: String): String {
		return apiLookupHelper.accountTypeLookup(accountType)
	}

	fun filingHistoryLookup(filingHistory: String): String {
		return apiLookupHelper.filingHistoryDescriptionLookup(filingHistory)
	}

	fun searchCompanies(queryText: CharSequence, startItem: String): Observable<CompanySearchResult> {
		return companiesHouseService.searchCompanies(authorization, queryText.toString(), BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		return preferencesHelper.addRecentSearch(searchHistoryItem)
	}

	fun getCompany(companyNumber: String): Observable<Company> {
		return companiesHouseService.getCompany(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getFilingHistory(companyNumber: String, category: String, startItem: String): Observable<FilingHistoryList> {
		return companiesHouseService.getFilingHistory(authorization, companyNumber, category, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getCharges(companyNumber: String, startItem: String): Observable<Charges> {
		return companiesHouseService.getCharges(authorization, companyNumber, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getInsolvency(companyNumber: String): Observable<Insolvency> {
		return companiesHouseService.getInsolvency(authorization, companyNumber)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getOfficers(companyNumber: String, startItem: String): Observable<Officers> {
		return companiesHouseService.getOfficers(authorization, companyNumber, null, null, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getOfficerAppointments(officerId: String, startItem: String): Observable<Appointments> {
		return companiesHouseService.getOfficerAppointments(authorization, officerId, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getPersons(companyNumber: String, startItem: String): Observable<Persons> {
		return companiesHouseService.getPersons(authorization, companyNumber, null, BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE, startItem)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun getDocument(filingHistoryItemString: String): Observable<ResponseBody> {
		val gson = Gson()
		val filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem::class.java)
		val documentId = filingHistoryItem.links.documentMetadata.replace("https://document-api.companieshouse.gov.uk/document/", "")
		return companiesHouseDocumentService.getDocument(authorization, "application/pdf", documentId)
				.subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
				.observeOn(AndroidSchedulers.mainThread())
	}

	fun writeDocumentPdf(responseBody: ResponseBody): Uri {
		val root = Environment.getExternalStorageDirectory()
		val dir = File(root.absolutePath + "/download")
		dir.mkdirs()
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

		return Uri.fromFile(pdfFile)
	}

	fun clearAllRecentSearches() {
		preferencesHelper.clearAllRecentSearches()
	}

	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		return preferencesHelper.addFavourite(searchHistoryItem)
	}

	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean {
		val items = preferencesHelper.favourites
		return if (items != null) {
			val favourites = ArrayList(Arrays.asList(*items))
			favourites.contains(searchHistoryItem)
		} else {
			false
		}
	}

	fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		preferencesHelper.removeFavourite(favouriteToRemove)
	}
}
