package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.common.model.common.MonthYear
import com.babestudios.companyinfouk.common.model.officers.OfficersResponse
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

	private var officersResponseYouLimited : OfficersResponse? = null

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
		officersResponseYouLimited = testDataComponent?.officersMapper()?.invoke(officersResponseDto)
	}

	@Test
	fun `when there is a company json then it is mapped`() {
		officersResponseYouLimited?.totalResults shouldBe 7
		officersResponseYouLimited?.items?.get(0)?.name shouldBe "STEVENSON, Elizabeth Mary"
		officersResponseYouLimited?.items?.get(0)?.dateOfBirth shouldBe MonthYear(null, null)
		officersResponseYouLimited?.items?.get(1)?.dateOfBirth shouldBe MonthYear(year = 1985, month = 7)
	}

	@Test
	fun `when appointedOn is not null then it should map to the same date`() {
		officersResponseYouLimited?.items?.get(1)?.appointedOn shouldBe "2017-09-02"
	}

	@Test
	fun `when appointedOn is null then it should map to Unknown`() {
		officersResponseYouLimited?.items?.get(0)?.appointedOn shouldBe "Unknown"
	}

	@Test
	fun `when resignedOn is not null then it should map to from to date`() {
		officersResponseYouLimited?.items?.get(3)?.fromToString shouldBe "From 2002-07-02 to 2002-07-02"
	}

	@Test
	fun `when resignedOn is null then it should map to from to date`() {
		officersResponseYouLimited?.items?.get(1)?.fromToString shouldBe "From 2017-09-02"
	}

}
