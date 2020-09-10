package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class CompanyMappersTest {

	private var testDataComponent: TestDataComponent? = null

	private val json = this.loadJson("company_candour")

	private val companyDto: CompanyDto = Gson().fromJson(json, CompanyDto::class.java)

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
	}

	@Test
	fun `when there is a company json then it is mapped`() {
		val companyCandour = testDataComponent?.companyMapper()?.invoke(companyDto)
		companyCandour?.natureOfBusiness shouldBe "68100 Buying and selling of own real estate"
		companyCandour?.lastAccountsMadeUpTo shouldBe "Last account made up to 31 Mar 2019"
		companyCandour?.companyName shouldBe "CANDOUR GROUP LIMITED"
		companyCandour?.registeredOfficeAddress?.addressLine1 shouldBe "71 New Dover Road"
	}

	@Test
	fun `when addressline2 is null then it is mapped to null`() {
		val companyCandour = testDataComponent?.companyMapper()?.invoke(companyDto)
		companyCandour?.registeredOfficeAddress?.addressLine2 shouldBe null
	}
}
