package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.MapperModule
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.domain.util.CoroutineContextModule
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AppointmentsMappersTest {

	private var testDataComponent: TestDataComponent? = null

	private val json = this.loadJson("appointments_allan_david_horley")

	private val appointmentsResponseDto = Gson().fromJson(json, AppointmentsResponseDto::class.java)

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
	fun `when there is a company json then it is mapped`() {
		val appointmentsResponseAllanDavidHorley =
			testDataComponent?.companiesHouseMapping()?.mapAppointments(appointmentsResponseDto)
		appointmentsResponseAllanDavidHorley?.totalResults shouldBe 3
		appointmentsResponseAllanDavidHorley?.items?.get(0)?.name shouldBe "Allan David HORLEY"
	}

}
