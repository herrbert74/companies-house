package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.data.network.CompaniesHouseService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import io.reactivex.observers.TestObserver
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

/**
 * This test is as brittle as it gets. Can return different results from time to time, but let's just keep it here.
 */
@RunWith(JUnit4::class)
class RetrofitTest {

	private var authorization: String? = null

	@Before
	fun setUp() {
		authorization = "Basic WnBoWHBnLXRyZndBTmlUTmZlNHh3SzZRWFk0WHdSd3cwd0h4RjVkbQ=="
	}

	@Test
	@Throws(Exception::class)
	fun testSearchCompanies() {
		val testSubscriber = TestObserver<String>()

		val retrofit = Retrofit.Builder()//
				.baseUrl(BuildConfig.COMPANIES_HOUSE_BASE_URL)//
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//
				//.callbackExecutor(Executors.newSingleThreadExecutor())//
				.addConverterFactory(AdvancedGsonConverterFactory.create())//
				.build()

		val companiesHouseService = retrofit.create(CompaniesHouseService::class.java)

		companiesHouseService.searchCompanies(authorization!!, "GAMES", "100", "0")
				.map<String> { e -> e.items[0].title }
				.subscribe(testSubscriber)
		val result = testSubscriber.events[0]
		testSubscriber.assertValue("DUNCANMC 123 LTD")
		assertThat(result[0] == "DUNCANMC 123 LTD", `is`(true))
		//testSubscriber.assertValue("CELESTIAL GAMES & BOOKS LTD")//GAMES AGENCY LIMITED");
		//assertThat(result[0] == "CELESTIAL GAMES & BOOKS LTD", `is`(true))//GAMES AGENCY LIMITED")
		testSubscriber.assertNoErrors()

	}
}
