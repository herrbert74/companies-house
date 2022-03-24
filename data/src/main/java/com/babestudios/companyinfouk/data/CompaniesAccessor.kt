package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.ext.getSerializedName
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
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
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
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.google.firebase.analytics.FirebaseAnalytics
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

@Singleton
open class CompaniesAccessor @Inject constructor(
	@ApplicationContext private val context: Context,
	private val companiesHouseService: CompaniesHouseService,
	private val companiesHouseDocumentService: CompaniesHouseDocumentService,
	private var preferencesHelper: PreferencesHelper,
	private val firebaseAnalytics: FirebaseAnalytics,
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
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CompaniesRepository {

	override suspend fun recentSearches(): List<SearchHistoryItem> {
		return preferencesHelper.recentSearches
	}


	override suspend fun favourites(): List<SearchHistoryItem> {
		return preferencesHelper.favourites.toList()
	}

	override suspend fun searchCompanies(queryText: CharSequence, startItem: String): CompanySearchResult {
		return companiesHouseService
			.searchCompanies(
				queryText.toString(),
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
	}

	override fun addRecentSearchItem(searchHistoryItem: SearchHistoryItem): ArrayList<SearchHistoryItem> {
		return preferencesHelper.addRecentSearch(searchHistoryItem)
	}

	override suspend fun getCompany(companyNumber: String): Company {
		return withContext(ioContext) {
			val companyDto = companiesHouseService.getCompany(companyNumber)
			mapCompanyDto(companyDto)
		}
	}

	override suspend fun getFilingHistory(
		companyNumber: String,
		category: Category,
		startItem: String
	): FilingHistory {
		return withContext(ioContext) {
			val historyDto = companiesHouseService.getFilingHistory(
				companyNumber,
				mapFilingHistoryCategory(category).getSerializedName(),
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			mapFilingHistoryDto(historyDto)
		}
	}

	override suspend fun fetchCharges(companyNumber: String, startItem: String): Charges {
		return withContext(ioContext) {
			val chargesDto = companiesHouseService.getCharges(
				companyNumber,
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			mapChargesDto(chargesDto)
		}
	}

	override suspend fun getInsolvency(companyNumber: String): Insolvency {
		return withContext(ioContext) {
			val insolvencyDto = companiesHouseService.getInsolvency(companyNumber)
			mapInsolvencyDto(insolvencyDto)
		}
	}

	override suspend fun getOfficers(companyNumber: String, startItem: String): OfficersResponse {
		return withContext(ioContext) {
			val officersResponseDto = companiesHouseService.getOfficers(
				companyNumber,
				null,
				null,
				null,
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			mapOfficersResponseDto(officersResponseDto)
		}
	}

	override suspend fun getOfficerAppointments(officerId: String, startItem: String): AppointmentsResponse {
		return withContext(ioContext) {
			val appointmentsResponseDto = companiesHouseService.getOfficerAppointments(
				officerId,
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			mapAppointmentsResponseDto(appointmentsResponseDto)
		}
	}

	override suspend fun getPersons(companyNumber: String, startItem: String): PersonsResponse {
		return withContext(ioContext) {
			val personsResponseDto = companiesHouseService.getPersons(
				companyNumber,
				null,
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			mapPersonsResponseDto(personsResponseDto)
		}
	}

	override suspend fun getCorporatePerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseService.getCorporatePerson(companyNumber, pscId)
			mapPersonDto(personDto)
		}
	}

	override suspend fun getLegalPerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseService.getLegalPerson(companyNumber, pscId)
			mapPersonDto(personDto)
		}
	}

	override suspend fun getDocument(documentId: String): ResponseBody {
		return withContext(ioContext) {
			companiesHouseDocumentService.getDocument("application/pdf", documentId)
		}
	}

	@Suppress("BlockingMethodInNonBlockingContext") // https://youtrack.jetbrains.com/issue/KTIJ-838
	override suspend fun writeDocumentPdf(responseBody: ResponseBody, uri: Uri): Uri {
		return withContext(ioContext) {
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
				Log.e("test", "File not found: ${e.localizedMessage}")
			} catch (e: IOException) {
				Log.d("test", "Error during closing input stream ${e.localizedMessage}")
			} finally {
				inputStream.close()

				outputStream?.close()
			}
			uri
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
