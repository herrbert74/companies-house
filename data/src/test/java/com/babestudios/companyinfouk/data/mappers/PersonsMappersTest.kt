package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class PersonsMappersTest {

	private var testDataComponent: TestDataComponent? = null

	private val json = this.loadJson("persons_yorkshire_air_ambulance")

	private val personsResponseDto: PersonsResponseDto = Gson().fromJson(json, PersonsResponseDto::class.java)

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
	}

	@Test
	fun `when there is a company json then it is mapped`() {
		val officersResponseYouLimited = testDataComponent?.personsMapper()?.invoke(personsResponseDto)
		officersResponseYouLimited?.totalResults shouldBe 5
		officersResponseYouLimited?.items?.get(0)?.name shouldBe "Mr Peter Sunderland"
	}

}
