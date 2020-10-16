package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.common.model.common.MonthYear
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class OfficersMappersTest {

	private var testDataComponent: TestDataComponent? = null

	private val json = this.loadJson("officers_you_limited")

	private val officersResponseDto: OfficersResponseDto = Gson().fromJson(json, OfficersResponseDto::class.java)

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
	}

	@Test
	fun `when there is a company json then it is mapped`() {
		val officersResponseYouLimited = testDataComponent?.officersMapper()?.invoke(officersResponseDto)
		officersResponseYouLimited?.totalResults shouldBe 7
		officersResponseYouLimited?.items?.get(0)?.name shouldBe "STEVENSON, Elizabeth Mary"
		officersResponseYouLimited?.items?.get(0)?.dateOfBirth shouldBe MonthYear(null, null)
		officersResponseYouLimited?.items?.get(1)?.dateOfBirth shouldBe MonthYear(year = 1985, month = 7)
	}

}
