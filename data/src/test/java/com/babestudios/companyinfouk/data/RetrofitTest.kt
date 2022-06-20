package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit


@RunWith(JUnit4::class)
class RetrofitTest {

	private val server = MockWebServer()
	private val baseUrl = server.url("/").toString()
	private lateinit var mockedResponse: String

	@Before
	fun setUp() {
		mockedResponse = this.loadJson("search_result_you")
	}

	@Test
	@Throws(Exception::class)
	fun testSearchCompanies() = runTest {
		server.enqueue(
			MockResponse()
				.setResponseCode(200)
				.setBody(mockedResponse)
		)
		val retrofit = Retrofit.Builder()
				.baseUrl(baseUrl)
				.addConverterFactory(AdvancedGsonConverterFactory.create())
				.build()

		val companiesHouseService = retrofit.create(CompaniesHouseService::class.java)

		val title = companiesHouseService.searchCompanies("GAMES", "100", "0")
			.items[0].title

		title shouldBe "YOU  LIMITED"

	}
}
