package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.MapperModule
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.domain.util.CoroutineContextModule
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class InsolvencyMappersTest {

	private var testDataComponent: TestDataComponent? = null

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
			.factory()
			.create(
				TestDataModule(context),
				MapperModule(),
				CoroutineContextModule(),
				context
			)
	}

	@Test
	fun `when there is a charges json then it is mapped`() {
		val json = this.loadJson("insolvency_london_airways")
		val insolvencyDto = Gson().fromJson(json, InsolvencyDto::class.java)
		val insolvencyLondonAirways = testDataComponent?.companiesHouseMapping()?.mapInsolvency(insolvencyDto)
		insolvencyLondonAirways?.cases?.get(0)?.dates?.get(0)?.date shouldBe "1995-01-18"
		insolvencyLondonAirways?.cases?.get(0)?.practitioners?.get(0)?.name shouldBe "Alan Redvers Price"
	}
}
