package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.google.gson.Gson
import io.mockk.mockk
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class CompanyMappersTest {

	private var testDataComponent: TestDataComponent? = null

	@Before
	fun setup() {
		val context = mockk<Context>()
		testDataComponent = DaggerTestDataComponent
				.factory()
				.create(TestDataModule(context), context)
	}

	@Test
	fun `when there is a company json then it is mapped`() {
		val json = this.loadJson("company_candour")
		val companyDto = Gson().fromJson(json, CompanyDto::class.java)
		val companyCandour = testDataComponent?.companyMapper()?.invoke(companyDto)
		assertThat(companyCandour?.natureOfBusiness, `is`("68100 Buying and selling of own real estate"))
		assertThat(companyCandour?.lastAccountsMadeUpTo, `is`("Last account made up to 31 Mar 2019"))
		assertThat(companyCandour?.companyName, `is`("CANDOUR GROUP LIMITED"))
		assertThat(companyCandour?.registeredOfficeAddress?.addressLine1, `is`("71 New Dover Road"))
	}
}
