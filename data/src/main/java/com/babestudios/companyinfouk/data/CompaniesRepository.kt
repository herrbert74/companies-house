package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.babestudios.base.data.AnalyticsContract
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.common.model.charges.Charges
import com.babestudios.companyinfouk.common.model.company.Company
import com.babestudios.companyinfouk.common.model.filinghistory.Category
import com.babestudios.companyinfouk.common.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.common.model.insolvency.Insolvency
import com.babestudios.companyinfouk.common.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.common.model.officers.OfficersResponse
import com.babestudios.companyinfouk.common.model.persons.Person
import com.babestudios.companyinfouk.common.model.persons.PersonsResponse
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategory
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
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

@Singleton
open class CompaniesRepository @Inject constructor(
		@ApplicationContext private val context: Context,
		private val companiesHouseService: CompaniesHouseService,
		private val companiesHouseDocumentService: CompaniesHouseDocumentService,
		private var preferencesHelper: PreferencesHelper,
		base64Wrapper: Base64Wrapper,
		private val firebaseAnalytics: FirebaseAnalytics,
		private val schedulerProvider: SchedulerProvider,
		private val errorResolver: ErrorResolver,
		private val mapFilingHistoryDto
		: (@JvmSuppressWildcards FilingHistoryDto) -> @JvmSuppressWildcards FilingHistory,
		private val mapChargesDto: (@JvmSuppressWildcards ChargesDto) -> @JvmSuppressWildcards Charges,
		private val mapCompanyDto: (@JvmSuppressWildcards CompanyDto) -> @JvmSuppressWildcards Company,
		private val mapInsolvencyDto: (@JvmSuppressWildcards InsolvencyDto) -> @JvmSuppressWildcards Insolvency,
		private val mapOfficersResponseDto
		: (@JvmSuppressWildcards OfficersResponseDto) -> @JvmSuppressWildcards OfficersResponse,
		private val mapAppointmentsResponseDto
		: (@JvmSuppressWildcards AppointmentsResponseDto) -> @JvmSuppressWildcards AppointmentsResponse,
		private val mapPersonsResponseDto
		: (@JvmSuppressWildcards PersonsResponseDto) -> @JvmSuppressWildcards PersonsResponse,
		private val mapPersonDto: (@JvmSuppressWildcards PersonDto) -> @JvmSuppressWildcards Person,
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
				.map { insolvencyDto ->
					mapInsolvencyDto(insolvencyDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficers(companyNumber: String, startItem: String): Single<OfficersResponse> {
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
				.map { officersResponseDto ->
					mapOfficersResponseDto(officersResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficerAppointments(officerId: String, startItem: String): Single<AppointmentsResponse> {
		return companiesHouseService
				.getOfficerAppointments(
						authorization,
						officerId,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { appointmentsResponseDto ->
					mapAppointmentsResponseDto(appointmentsResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getPersons(companyNumber: String, startItem: String): Single<PersonsResponse> {
		return companiesHouseService
				.getPersons(
						authorization,
						companyNumber,
						null,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				).compose(errorResolver.resolveErrorForSingle())
				.map { personsResponseDto ->
					mapPersonsResponseDto(personsResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getCorporatePerson(companyNumber: String, pscId: String): Single<Person> {
		return companiesHouseService
				.getCorporatePerson(
						authorization,
						companyNumber,
						pscId,
				).compose(errorResolver.resolveErrorForSingle())
				.map { personDto ->
					mapPersonDto(personDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getLegalPerson(companyNumber: String, pscId: String): Single<Person> {
		return companiesHouseService
				.getLegalPerson(
						authorization,
						companyNumber,
						pscId,
				).compose(errorResolver.resolveErrorForSingle())
				.map { personDto ->
					mapPersonDto(personDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getDocument(documentId: String): Single<ResponseBody> {
		return companiesHouseDocumentService.getDocument(authorization, "application/pdf", documentId)
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun writeDocumentPdf(responseBody: ResponseBody, uri: Uri): Single<Uri> {
		val outputStream = context.contentResolver.openOutputStream(uri)
		val inputStream = responseBody.byteStream()
		try {
			@Suppress("MagicNumber")
			val fileReader = ByteArray(4096)
				while (true) {
					val read = inputStream.read(fileReader)
					if (read == -1) {
						break
					}
					outputStream?.write(fileReader, 0, read)
				}
				outputStream?.flush()
		} catch (e: FileNotFoundException) {
			e.printStackTrace()
		} catch (e: IOException) {
			Log.d("test", "Error during closing input stream" + e.localizedMessage)
		} finally {
				inputStream.close()

				outputStream?.close()
			}
		return Single.just(uri)
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
