package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ChargesMappersTest {

	private var testDataComponent: TestDataComponent? = null

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
	}

	@Test
	fun `when there is a charges json then it is mapped`() {
		val json = this.loadJson("charges_pfb_hire")
		val chargesDto = Gson().fromJson(json, ChargesDto::class.java)
		val chargesPfbHire = testDataComponent?.chargesMapper()?.invoke(chargesDto)
		chargesPfbHire?.totalCount shouldBe 9
		chargesPfbHire?.items?.get(0)?.personsEntitled shouldContain "Art Share"
	}
}
