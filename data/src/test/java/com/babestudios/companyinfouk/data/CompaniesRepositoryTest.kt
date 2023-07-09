package com.babestudios.companyinfouk.data

import android.content.Context
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseDocumentApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.google.firebase.analytics.FirebaseAnalytics
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CompaniesRepositoryTest {

	private val context = mockk<Context>()

	private val mockCompaniesHouseApi = mockk<CompaniesHouseApi>()

	private val mockCompaniesHouseDocumentApi = mockk<CompaniesHouseDocumentApi>()

	private val mockPreferencesHelper = mockk<PreferencesHelper>()

	private val firebaseAnalytics = mockk<FirebaseAnalytics>()

	private lateinit var companiesRepository: CompaniesRepository

	private val companiesHouseMapping = mockk<CompaniesHouseMapping>()

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val companySearchResult = CompanySearchResult()

	@Before
	fun setUp() {
		companiesRepository = CompaniesAccessor(
			context,
			mockCompaniesHouseApi,
			mockCompaniesHouseDocumentApi,
			mockPreferencesHelper,
			firebaseAnalytics,
			companiesHouseMapping,
			testCoroutineDispatcher,
		)

		coEvery {mockCompaniesHouseApi.searchCompanies("Games", "50","0")
		} answers { companySearchResult }
	}

	@Test
	fun `when searchCompanies then calls service and returns result`() = runTest {
		val data = companiesRepository.searchCompanies("Games", "0")
		coVerify { mockCompaniesHouseApi.searchCompanies(
			"Games",
			COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
			"0"
		) }
		data shouldBe companySearchResult
	}
}
