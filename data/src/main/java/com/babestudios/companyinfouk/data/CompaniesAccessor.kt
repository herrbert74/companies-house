package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.network.OfflineException
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategory
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.Charges
import com.babestudios.companyinfouk.domain.model.common.ApiResult
import com.babestudios.companyinfouk.domain.model.common.apiRunCatching
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
import com.github.michaelbull.result.mapError
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
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
	private val companiesHouseMapping: CompaniesHouseMapping,
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
			companiesHouseMapping.mapCompany(companyDto)
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
			companiesHouseMapping.mapFilingHistory(historyDto)
		}
	}

	override suspend fun getCharges(companyNumber: String, startItem: String): ApiResult<Charges> {
		return apiRunCatching {
			withContext(ioContext) {
				val chargesDto = companiesHouseService.getCharges(
					companyNumber,
					BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				companiesHouseMapping.mapChargesHistory(chargesDto)
			}
		}.mapError { OfflineException(Charges()) }
	}

	override suspend fun getInsolvency(companyNumber: String): ApiResult<Insolvency> {
		return apiRunCatching {
			withContext(ioContext) {
				val insolvencyDto = companiesHouseService.getInsolvency(companyNumber)
				companiesHouseMapping.mapInsolvency(insolvencyDto)
			}
		}.mapError { OfflineException(Insolvency()) }
	}

	override suspend fun getOfficers(companyNumber: String, startItem: String): ApiResult<OfficersResponse> {

		return apiRunCatching {
			withContext(ioContext) {
				val officersResponseDto = companiesHouseService.getOfficers(
					companyNumber,
					null,
					null,
					null,
					BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				companiesHouseMapping.mapOfficers(officersResponseDto)
			}
		}.mapError { OfflineException(OfficersResponse()) }

	}

	override suspend fun getOfficerAppointments(officerId: String, startItem: String): AppointmentsResponse {
		return withContext(ioContext) {
			val appointmentsResponseDto = companiesHouseService.getOfficerAppointments(
				officerId,
				BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				startItem
			)
			companiesHouseMapping.mapAppointments(appointmentsResponseDto)
		}
	}

	override suspend fun getPersons(companyNumber: String, startItem: String): ApiResult<PersonsResponse> {
		return apiRunCatching {
			withContext(ioContext) {
				val personsResponseDto = companiesHouseService.getPersons(
					companyNumber,
					null,
					BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
					startItem
				)
				companiesHouseMapping.mapPersonsResponse(personsResponseDto)
			}
		}.mapError { OfflineException(PersonsResponse()) }
	}

	override suspend fun getCorporatePerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseService.getCorporatePerson(companyNumber, pscId)
			companiesHouseMapping.mapPerson(personDto)
		}
	}

	override suspend fun getLegalPerson(companyNumber: String, pscId: String): Person {
		return withContext(ioContext) {
			val personDto = companiesHouseService.getLegalPerson(companyNumber, pscId)
			companiesHouseMapping.mapPerson(personDto)
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
