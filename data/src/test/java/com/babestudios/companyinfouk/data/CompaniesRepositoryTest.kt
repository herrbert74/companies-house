package com.babestudios.companyinfouk.data

import android.content.Context
import com.babestudios.companyinfouk.data.local.PreferencesHelper
import com.babestudios.companyinfouk.data.mappers.CompaniesHouseMapping
import com.babestudios.companyinfouk.data.network.CompaniesHouseDocumentService
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
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

	private val mockCompaniesHouseService = mockk<CompaniesHouseService>()

	private val mockCompaniesHouseDocumentService = mockk<CompaniesHouseDocumentService>()

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
			mockCompaniesHouseService,
			mockCompaniesHouseDocumentService,
			mockPreferencesHelper,
			firebaseAnalytics,
			companiesHouseMapping,
			testCoroutineDispatcher,
		)

		coEvery {mockCompaniesHouseService.searchCompanies("Games", "50","0")
		} answers { companySearchResult }
	}

	@Test
	fun `when searchCompanies then calls service and returns result`() = runTest {
		val data = companiesRepository.searchCompanies("Games", "0")
		coVerify { mockCompaniesHouseService.searchCompanies(
			"Games",
			BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
			"0"
		) }
		data shouldBe companySearchResult
	}
}
