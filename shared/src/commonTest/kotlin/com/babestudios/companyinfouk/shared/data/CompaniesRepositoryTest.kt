package com.babestudios.companyinfouk.shared.data

import com.babestudios.companyinfouk.shared.AnalyticsFactory
import com.babestudios.companyinfouk.shared.data.local.Prefs
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import org.kodein.mock.Mock
import org.kodein.mock.Mocker
import org.kodein.mock.UsesMocks

@UsesMocks(CompaniesHouseApi::class, Prefs::class)
class CompaniesRepositoryTest {

	@Mock
	lateinit var mockCompaniesHouseApi: CompaniesHouseApi

	@Mock
	lateinit var mockPrefsAccessor: Prefs

	private lateinit var companiesRepository: CompaniesRepository

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val companySearchResult = CompanySearchResult()

	private val mocker = Mocker()

	@BeforeTest
	fun setUp() {

		mocker.reset()
		this.injectMocks(mocker)

		companiesRepository = CompaniesAccessor(
			mockCompaniesHouseApi,
			mockPrefsAccessor,
			AnalyticsFactory(),
			testCoroutineDispatcher,
		)

	}

	@Test
	fun `when searchCompanies then calls service and returns result`() = runTest {

		mocker.everySuspending {
			mockCompaniesHouseApi.searchCompanies("Games", "50", "0")
		} returns companySearchResult

		val data = companiesRepository.searchCompanies("Games", "0")

		mocker.verifyWithSuspend {
			mockCompaniesHouseApi.searchCompanies(
				"Games",
				COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				"0"
			)
		}
		data shouldBe companySearchResult

	}

}
