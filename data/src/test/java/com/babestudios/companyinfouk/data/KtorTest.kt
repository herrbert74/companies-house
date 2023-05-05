package com.babestudios.companyinfouk.data

import com.babestudios.base.kotlin.ext.loadJson
import com.babestudios.companyinfouk.data.network.CompaniesHouseApi
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
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Just a minimal example of Ktor [MockEngine], in case we want to use lower level mocks.
 */
@RunWith(JUnit4::class)
class KtorTest {

	private lateinit var mockedResponse: String

	private lateinit var mockEngine: MockEngine
	private lateinit var client: HttpClient

	@Before
	fun setUp() {
		mockedResponse = this.loadJson("search_result_you")
		mockEngine = MockEngine {
			respond(
				content = mockedResponse,
				status = HttpStatusCode.OK,
				headers = headersOf(HttpHeaders.ContentType, "application/json")
			)
		}
		client = HttpClient(mockEngine) {
			install(ContentNegotiation) {
				json(Json {
					prettyPrint = true
					isLenient = true
					ignoreUnknownKeys = true
				})
			}
		}
	}

	@Test
	@Throws(Exception::class)
	fun testSearchCompanies() = runTest {

		val ktorfit = Ktorfit.Builder().httpClient(client).build()
		val companiesHouseService = ktorfit.create<CompaniesHouseApi>()

		val companySearchResult = companiesHouseService.searchCompanies("GAMES", "100", "0")

		companySearchResult.items[0].title shouldBe "YOU  LIMITED"

	}

}
