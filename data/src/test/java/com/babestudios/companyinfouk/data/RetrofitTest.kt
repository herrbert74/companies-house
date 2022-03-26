package com.babestudios.companyinfouk.data

import com.babestudios.companyinfouk.data.network.CompaniesHouseRxService
import com.babestudios.companyinfouk.data.network.converters.AdvancedGsonConverterFactory
import io.kotest.matchers.shouldBe
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


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

		val companiesHouseService = retrofit.create(CompaniesHouseRxService::class.java)

		companiesHouseService.searchCompanies(/*authorization!!,*/ "GAMES", "100", "0")
				.map<String> { e -> e.items[0].title }
				.subscribe(testSubscriber)
		val result = testSubscriber.events[0]
		result[0]::class.java shouldBe String::class.java
		testSubscriber.assertNoErrors()

	}
}
