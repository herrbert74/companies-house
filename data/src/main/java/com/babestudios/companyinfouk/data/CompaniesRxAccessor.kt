package com.babestudios.companyinfouk.data

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.ext.getSerializedName
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.mappers.mapFilingHistoryCategory
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseRxService
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
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
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.ResponseBody

@Singleton
open class CompaniesRxAccessor @Inject constructor(
	@ApplicationContext private val context: Context,
	private val companiesHouseService: CompaniesHouseRxService,
	private val companiesHouseDocumentService: CompaniesHouseRxDocumentService,
	private var preferencesHelper: PreferencesHelper,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val schedulerProvider: SchedulerProvider,
	private val errorResolver: ErrorResolver,
	private val companiesHouseMapping: CompaniesHouseMapping,
) : CompaniesRxRepository {

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
				.getCompany(companyNumber)
				.map { companiesHouseMapping.mapCompany(it) }
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
						companyNumber,
						mapFilingHistoryCategory(category).getSerializedName(),
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { historyDto ->
					companiesHouseMapping.mapFilingHistory(historyDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun fetchCharges(companyNumber: String, startItem: String): Single<Charges> {
		return companiesHouseService
				.getCharges(
						companyNumber,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { chargesDto ->
					companiesHouseMapping.mapChargesHistory(chargesDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getInsolvency(companyNumber: String): Single<Insolvency> {
		return companiesHouseService
				.getInsolvency(companyNumber)
				.compose(errorResolver.resolveErrorForSingle())
				.map { insolvencyDto ->
					companiesHouseMapping.mapInsolvency(insolvencyDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficers(companyNumber: String, startItem: String): Single<OfficersResponse> {
		return companiesHouseService
				.getOfficers(
						companyNumber,
						null,
						null,
						null,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { officersResponseDto ->
					companiesHouseMapping.mapOfficers(officersResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getOfficerAppointments(officerId: String, startItem: String): Single<AppointmentsResponse> {
		return companiesHouseService
				.getOfficerAppointments(
						officerId,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				)
				.compose(errorResolver.resolveErrorForSingle())
				.map { appointmentsResponseDto ->
					companiesHouseMapping.mapAppointments(appointmentsResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getPersons(companyNumber: String, startItem: String): Single<PersonsResponse> {
		return companiesHouseService
				.getPersons(
						companyNumber,
						null,
						BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
						startItem
				).compose(errorResolver.resolveErrorForSingle())
				.map { personsResponseDto ->
					companiesHouseMapping.mapPersonsResponse(personsResponseDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getCorporatePerson(companyNumber: String, pscId: String): Single<Person> {
		return companiesHouseService
				.getCorporatePerson(
						companyNumber,
						pscId,
				).compose(errorResolver.resolveErrorForSingle())
				.map { personDto ->
					companiesHouseMapping.mapPerson(personDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getLegalPerson(companyNumber: String, pscId: String): Single<Person> {
		return companiesHouseService
				.getLegalPerson(
						companyNumber,
						pscId,
				).compose(errorResolver.resolveErrorForSingle())
				.map { personDto ->
					companiesHouseMapping.mapPerson(personDto)
				}
				.compose(schedulerProvider.getSchedulersForSingle())
	}

	override fun getDocument(documentId: String): Single<ResponseBody> {
		return companiesHouseDocumentService.getDocument("application/pdf", documentId)
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
			Log.e("test", "File not found: ${e.localizedMessage}")
		} catch (e: IOException) {
			Log.d("test", "Error during closing input stream ${e.localizedMessage}")
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
