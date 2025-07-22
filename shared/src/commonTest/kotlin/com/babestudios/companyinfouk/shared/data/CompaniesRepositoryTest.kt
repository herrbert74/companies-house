package com.babestudios.companyinfouk.shared.data

import com.babestudios.companyinfouk.shared.AnalyticsFactory
import com.babestudios.companyinfouk.shared.data.local.Prefs
import com.babestudios.companyinfouk.shared.data.network.CompaniesHouseApi
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest

class CompaniesRepositoryTest {

	private val mockCompaniesHouseApi = mock<CompaniesHouseApi>()

	private val mockPrefsAccessor = mock<Prefs>()

	private lateinit var companiesRepository: CompaniesRepository

	private val testCoroutineDispatcher = Dispatchers.Unconfined

	private val companySearchResult = CompanySearchResult()

	@BeforeTest
	fun setUp() {
		companiesRepository = CompaniesAccessor(
			mockCompaniesHouseApi,
			mockPrefsAccessor,
			AnalyticsFactory(),
			testCoroutineDispatcher,
		)

	}

	@Test
	fun `when searchCompanies then calls service and returns result`() = runTest {
		everySuspend {
			mockCompaniesHouseApi.searchCompanies("Games", "50", "0")
		} returns companySearchResult

		val data = companiesRepository.searchCompanies("Games", "0")

		verifySuspend {
			mockCompaniesHouseApi.searchCompanies(
				"Games",
				COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE,
				"0"
			)
		}
		data shouldBe companySearchResult

	}

}
