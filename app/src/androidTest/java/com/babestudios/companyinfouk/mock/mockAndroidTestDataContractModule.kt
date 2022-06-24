package com.babestudios.companyinfouk.mock

import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.utils.RawResourceHelper
import com.babestudios.companyinfouk.data.utils.RawResourceHelperContract
import com.babestudios.companyinfouk.data.utils.errors.CompaniesHouseRxErrorResolver
import com.babestudios.companyinfouk.data.utils.errors.apilookup.ErrorHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem
import com.google.gson.GsonBuilder
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk

/**
 *
 * Replaces DataContractModule.
 * 
 * We uninstall the contract module and replace it with mocks from here in order to be
 * able to provide different response for companiesRepository in different tests.
 *
 */
fun mockCompaniesRepository(): CompaniesRepository {
	val gson = GsonBuilder()
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
		.create()

	val mockCompaniesRepository = mockk<CompaniesRepository>()

	val favourites = listOf(
		SearchHistoryItem(
			"Acme Painting",
			"1",
			111L
		)
	)

	coEvery {
		mockCompaniesRepository.favourites()
	} returns favourites

	every {
		mockCompaniesRepository.logAppOpen()
	} returns Unit

	every {
		mockCompaniesRepository.logScreenView(any())
	} returns Unit

	every {
		mockCompaniesRepository.logSearch(any())
	} returns Unit

	coEvery {
		mockCompaniesRepository.recentSearches()
	} returns favourites

	val jsonSearchResultForYou = gson.loadJson("search_result_you")
	val companySearchItemForYou = gson.fromJson(jsonSearchResultForYou, CompanySearchResult::class.java)
	coEvery {
		mockCompaniesRepository.searchCompanies(eq("you"), any())
	} returns companySearchItemForYou

	return mockCompaniesRepository

}

internal fun rawResourceHelperContract(@ApplicationContext context: Context): RawResourceHelperContract {
	return RawResourceHelper(context)
}

internal fun errorResolver(errorHelper: ErrorHelper): ErrorResolver {
	return CompaniesHouseRxErrorResolver(errorHelper)
}
