package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import com.babestudios.base.data.AnalyticsContract
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelper
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelper
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategory
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.utils.Base64Wrapper
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Single
import io.reactivex.SingleEmitter
import okhttp3.ResponseBody
import java.io.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

interface CompaniesRepositoryContract : AnalyticsContract {
	val authorization: String
	fun recentSearches(): Single<List<SearchHistoryItem>>
	fun favourites(): Single<List<SearchHistoryItem>>

	//String mapping
	fun sicLookup(code: String): String

	fun accountTypeLookup(accountType: String): String
	fun filingHistoryLookup(filingHistoryDescription: String): String

	//Companies House API
	fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult>

	fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem>
	fun getCompany(companyNumber: String): Single<Company>
	fun getFilingHistory(companyNumber: String, category: Category, startItem: String): Single<FilingHistory>
	fun fetchCharges(companyNumber: String, startItem: String): Single<Charges>
	fun getInsolvency(companyNumber: String): Single<Insolvency>
	fun getOfficers(companyNumber: String, startItem: String): Single<Officers>
	fun getOfficerAppointments(officerId: String, startItem: String): Single<Appointments>
	fun getPersons(companyNumber: String, startItem: String): Single<Persons>
	fun getDocument(documentId: String): Single<ResponseBody>
	fun writeDocumentPdf(responseBody: ResponseBody): Single<Uri>

	//Preferences
	fun clearAllRecentSearches()

	fun addFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun isFavourite(searchHistoryItem: SearchHistoryItem): Boolean
	fun removeFavourite(favouriteToRemove: SearchHistoryItem)
}

@Singleton
open class CompaniesRepository @Inject constructor(
		@ApplicationContext private val context: Context,
		private val companiesHouseService: CompaniesHouseService,
		private val companiesHouseDocumentService: CompaniesHouseDocumentService,
		private var preferencesHelper: PreferencesHelper,
		base64Wrapper: Base64Wrapper,
		private val constantsHelper: ConstantsHelper,
		private val filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelper,
		private val firebaseAnalytics: FirebaseAnalytics,
		private val schedulerProvider: SchedulerProvider,
		private val errorResolver: ErrorResolver,
		private val mapFilingHistoryDto: (@JvmSuppressWildcards FilingHistoryDto) -> @JvmSuppressWildcards
		FilingHistory,
		private val mapChargesDto: (@JvmSuppressWildcards ChargesDto) -> @JvmSuppressWildcards
		Charges,
		private val mapCompanyDto: (@JvmSuppressWildcards CompanyDto) -> @JvmSuppressWildcards
		Company,
) : CompaniesRepositoryContract {

	override val authorization: String = "Basic " + base64Wrapper.encodeToString(
			BuildConfig.COMPANIES_HOUSE_API_KEY.toByteArray(),
			Base64.NO_WRAP
	)

	override fun recentSearches(): Single<List<SearchHistoryItem>> {
		return Single.create { singleEmitter: SingleEmitter<List<SearchHistoryItem>> ->
			singleEmitter.onSuccess(preferencesHelper.recentSearches)
		}.compose(schedulerProvider.getSchedulersForSingle())
	}


	override fun favourites(): Single<List<SearchHistoryItem>> {
		return Single.create { singleEmitter: SingleEmitter<List<SearchHistoryItem>> ->
			singleEmitter.onSuccess(preferencesHelper.favourites.toList())

		}.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun sicLookup(code: String): String {
		return constantsHelper.sicLookUp(code)
	}

	override fun accountTypeLookup(accountType: String): String {
		return constantsHelper.accountTypeLookUp(accountType)
	}

	override fun filingHistoryLookup(filingHistoryDescription: String): String {
		return filingHistoryDescriptionsHelper.filingHistoryLookUp(filingHistoryDescription)
	}

	override fun searchCompanies(queryText: CharSequence, startItem: String): Single<CompanySearchResult> {
		return companiesHouseService
				.searchCompanies(
						authorization,
						queryText.toString(),
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		return preferencesHelper.addRecentSearch(searchHistoryItem)
	}

	override fun getCompany(companyNumber: String): Single<Company> {
		return companiesHouseService
				.getCompany(authorization, companyNumber)
				.map { mapCompanyDto(it) }
				.compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getFilingHistory(
			companyNumber: String,
			category: Category,
			startItem: String
	): Single<FilingHistory> {
		return companiesHouseService
				.getFilingHistory(
						authorization,
						companyNumber,
						mapFilingHistoryCategory(category).getSerializedName(),
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { historyDto ->
					mapFilingHistoryDto(historyDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun fetchCharges(companyNumber: String, startItem: String): Single<Charges> {
		return companiesHouseService
				.getCharges(
						authorization,
						companyNumber,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { chargesDto ->
					mapChargesDto(chargesDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getInsolvency(companyNumber: String): Single<Insolvency> {
		return companiesHouseService
				.getInsolvency(authorization, companyNumber)
				.compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficers(companyNumber: String, startItem: String): Single<Officers> {
		return companiesHouseService
				.getOfficers(
						authorization,
						companyNumber,
						null,
						null,
						null,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficerAppointments(officerId: String, startItem: String): Single<Appointments> {
		return companiesHouseService
				.getOfficerAppointments(
						authorization,
						officerId,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getPersons(companyNumber: String, startItem: String): Single<Persons> {
		return companiesHouseService
				.getPersons(
						authorization,
						companyNumber,
						null,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				).compose(errorResolver.resolveErrorForSingle())
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getDocument(documentId: String): Single<ResponseBody> {
		return companiesHouseDocumentService.getDocument(authorization, "application/pdf", documentId)
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	@Suppress("NestedBlockDepth")
	override fun writeDocumentPdf(responseBody: ResponseBody): Single<Uri> {
		val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
		val pdfFile = File(dir, "doc.pdf")
		var inputStream: InputStream? = null
		var outputStream: OutputStream? = null
		try {
			@Suppress("MagicNumber")
			val fileReader = ByteArray(4096)
			try {
				inputStream = responseBody.byteStream()
				outputStream = FileOutputStream(pdfFile)
				while (true) {
					val read = inputStream.read(fileReader)
					if (read == -1) {
						break
					}
					outputStream.write(fileReader, 0, read)
				}
				outputStream.flush()
			} catch (e: IOException) {
				Log.d("test", e.localizedMessage ?: "")
			} finally {
				inputStream?.close()

				outputStream?.close()
			}
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		} catch (e: IOException) {
			Log.d("test", "Error during closing input stream" + e.localizedMessage)
		}

		return Single.create { singleEmitter: SingleEmitter<Uri> ->
			val async = FileProvider.getUriForFile(
					context,
					context.packageName + ".fileprovider",
					pdfFile
			)
			singleEmitter.onSuccess(async)
		}.compose(schedulerProvider.getSchedulersForSingle())
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
