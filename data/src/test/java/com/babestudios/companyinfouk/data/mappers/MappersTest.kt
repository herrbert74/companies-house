package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.model.filinghistory.Capital
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Test

class MappersTest {

	@Test
	fun `when there is a place holder in filing history item then it is mapped`() {
		val description = "test {replace}"
		val descriptionValues = mapOf("replace" to "value")
		val res = formatFilingHistoryDescriptionDto(description, descriptionValues)
		assertThat( res, `is`("test value"))
	}

	@Test
	fun `when there is a capital place holder in filing history item then it is not mapped`() {
		val description = "**Statement of capital following an allotment of shares** on {date}"
		val descriptionValues = mapOf("date" to "01/01/2020", "capital" to Capital("11.1", "GBP"))
		val res = formatFilingHistoryDescriptionDto(description, descriptionValues)
		assertThat( res, `is`("**Statement of capital following an allotment of shares** on 01/01/2020"))
	}

	@Test
	fun `when there is a capital place holder in filing history item then it is not mapped 1`() {
		val description = "**Registered office address changed** from {old_address} to {new_address} on {change_date}"
		val descriptionValues = mapOf("new_address" to "20-22 Wenlock Road London N1 7GU",
				"old_address" to "99 Evesham Road London N11 2RR England",
				"change_date" to "2020-07-08"
		)
		val res = formatFilingHistoryDescriptionDto(description, descriptionValues)
		assertThat( res, `is`("**Registered office address changed** from 99 Evesham Road London N11 2RR England to 20-22 Wenlock Road London N1 7GU on 2020-07-08"))
	}
}
