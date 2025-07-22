package com.babestudios.companyinfouk.shared.data

import com.babestudios.base.kotlin.io.readCommonResource
import com.babestudios.companyinfouk.shared.data.network.createCompaniesHouseApi
import de.jensklingenberg.ktorfit.Ktorfit
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json

/**
 * Just a minimal example of Ktor [MockEngine], in case we want to use lower level mocks.
 */
class KtorTest {

	private lateinit var mockedResponse: String

	private lateinit var mockEngine: MockEngine
	private lateinit var client: HttpClient

	@BeforeTest
	fun setUp() {
		mockedResponse = readCommonResource("search_result_you.json")
		mockEngine = MockEngine {
			respond(
				content = mockedResponse,
				status = HttpStatusCode.OK,
				headers = headersOf(HttpHeaders.ContentType, "application/json")
			)
		}
		client = HttpClient(mockEngine) {
			install(ContentNegotiation) {
				json(
					Json {
						prettyPrint = true
						isLenient = true
						ignoreUnknownKeys = true
					}
				)
			}
		}
	}

	@Test
	@Throws(Exception::class)
	fun testSearchCompanies() = runTest {
		val ktorfit = Ktorfit.Builder().httpClient(client).build()
		val companiesHouseService = ktorfit.createCompaniesHouseApi()

		val companySearchResult = companiesHouseService.searchCompanies("GAMES", "100", "0")

		companySearchResult.items[0].title shouldBe "YOU  LIMITED"

	}

}
