package com.babestudios.companyinfouk.data.mappers

import android.content.Context
import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.di.DaggerTestDataComponent
import com.babestudios.companyinfouk.data.di.MapperModule
import com.babestudios.companyinfouk.data.di.TestDataComponent
import com.babestudios.companyinfouk.data.di.TestDataModule
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.domain.model.filinghistory.Capital
import com.babestudios.companyinfouk.domain.util.CoroutineContextModule
import com.google.gson.Gson
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class FilingHistoryMappersTest {

	//region Full Filing History map

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
	fun `when there is a filing history json then it is mapped`() {
		val json = this.loadJson("filing_pfb_hire")
		val filingHistoryDto = Gson().fromJson(json, FilingHistoryDto::class.java)
		val filingHistoryPfbHire = testDataComponent?.companiesHouseMapping()?.mapFilingHistory(filingHistoryDto)
		filingHistoryPfbHire?.totalCount shouldBe 47
		filingHistoryPfbHire?.filingHistoryStatus shouldBe "filing-history-available"
	}

	//endregion

	//region format filing description

	@Test
	fun `when there is a place holder in filing history item then it is mapped`() {
		val description = "test {replace}"
		val descriptionValues = mapOf("replace" to "value")
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "test value"
	}

	@Test
	fun `when there is a capital place holder in filing history item then it is not mapped`() {
		val description = "**Statement of capital following an allotment of shares** on {date}"
		val descriptionValues = mapOf("date" to "01/01/2020", "capital" to Capital("11.1", "GBP"))
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "**Statement of capital following an allotment of shares** on 01/01/2020"
	}

	@Test
	fun `when there are three place holders in filing history item then it is mapped`() {
		val description = "**Registered office address changed** from {old_address} to {new_address} on {change_date}"
		val descriptionValues = mapOf(
			"new_address" to "20-22 Wenlock Road London N1 7GU",
			"old_address" to "99 Evesham Road London N11 2RR England",
			"change_date" to "2020-07-08"
		)
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "**Registered office address changed** from 99 Evesham Road London N11 2RR England to 20-22 " +
			"Wenlock Road London N1 7GU on 2020-07-08"
	}

	/**
	 * Real History Item for the company "Naked Elephant", 06398386
	 */
	@Test
	fun `when there is an invalid regex in legacy filing history item then it is not causing an exception`() {
		val description = "legacy"
		val descriptionValues = mapOf("description" to "Ad 01/01/08\\gbp si 10@1=10\\gbp ic 100/110\\")
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "Ad 01/01/08\\gbp si 10@1=10\\gbp ic 100/110\\"
	}

	//endregion
}
