package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.common.loadJson
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import com.babestudios.companyinfouk.domain.model.filinghistory.Capital
import com.babestudios.companyinfouk.domain.model.officers.OfficersResponse
import com.google.gson.Gson
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import javax.inject.Inject
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class MappersTest {

	@get:Rule
	var hiltAndroidRule = HiltAndroidRule(this)

	@Inject
	lateinit var companiesHouseMapper: CompaniesHouseMapping

	private lateinit var officersResponseYouLimited: OfficersResponse

	@Before
	fun setup() {
		hiltAndroidRule.inject()
		officersResponseYouLimited = companiesHouseMapper.mapOfficers(officersResponseDto)
	}

	//region Appointments mapping

	private val appointmentsJson = this.loadJson("appointments_allan_david_horley")
	private val appointmentsResponseDto = Gson().fromJson(appointmentsJson, AppointmentsResponseDto::class.java)

	@Test
	fun `when there is an appointment json then it is mapped`() {
		val appointmentsResponseAllanDavidHorley =
			companiesHouseMapper.mapAppointments(appointmentsResponseDto)
		appointmentsResponseAllanDavidHorley.totalResults shouldBe 3
		appointmentsResponseAllanDavidHorley.items?.get(0)?.name shouldBe "Allan David HORLEY"
	}

	//endregion

	//region Charges mapping

	@Test
	fun `when there is a charges json then it is mapped`() {
		val json = this.loadJson("charges_pfb_hire")
		val chargesDto = Gson().fromJson(json, ChargesDto::class.java)
		val chargesPfbHire = companiesHouseMapper.mapChargesHistory(chargesDto)
		chargesPfbHire.totalCount shouldBe 9
		chargesPfbHire.items[0].personsEntitled shouldContain "Art Share"
	}

	//endregion

	//region Company mapping

	private val companyJson = this.loadJson("company_candour")
	private val companyDto: CompanyDto = Gson().fromJson(companyJson, CompanyDto::class.java)

	@Test
	fun `when there is a company json then it is mapped`() {
		val companyCandour = companiesHouseMapper.mapCompany(companyDto)
		companyCandour.natureOfBusiness shouldBe "68100 Buying and selling of own real estate"
		companyCandour.lastAccountsMadeUpTo shouldBe "Last account made up to 31 Mar 2019"
		companyCandour.companyName shouldBe "CANDOUR GROUP LIMITED"
		companyCandour.registeredOfficeAddress.addressLine1 shouldBe "71 New Dover Road"
	}

	@Test
	fun `when addressline2 is null then it is mapped to null`() {
		val companyCandour = companiesHouseMapper.mapCompany(companyDto)
		companyCandour.registeredOfficeAddress.addressLine2 shouldBe null
	}

	//endregion

	//region Full Filing History map

	@Test
	fun `when there is a filing history json then it is mapped`() {
		val json = this.loadJson("filing_pfb_hire")
		val filingHistoryDto = Gson().fromJson(json, FilingHistoryDto::class.java)
		val filingHistoryPfbHire = companiesHouseMapper.mapFilingHistory(filingHistoryDto)
		filingHistoryPfbHire.totalCount shouldBe 47
		filingHistoryPfbHire.filingHistoryStatus shouldBe "filing-history-available"
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

	//region Insolvency mapping

	@Test
	fun `when there is an insolvency json then it is mapped`() {
		val json = this.loadJson("insolvency_london_airways")
		val insolvencyDto = Gson().fromJson(json, InsolvencyDto::class.java)
		val insolvencyLondonAirways = companiesHouseMapper.mapInsolvency(insolvencyDto)
		insolvencyLondonAirways.cases[0].dates[0].date shouldBe "1995-01-18"
		insolvencyLondonAirways.cases[0].practitioners[0].name shouldBe "Alan Redvers Price"
	}

	//endregion

	//region Officer mapping

	private val officersJson = this.loadJson("officers_you_limited")
	private val officersResponseDto: OfficersResponseDto = Gson().fromJson(officersJson, OfficersResponseDto::class.java)

	@Test
	fun `when there is an officer json then it is mapped`() {
		officersResponseYouLimited.totalResults shouldBe 7
		officersResponseYouLimited.items[0].name shouldBe "STEVENSON, Elizabeth Mary"
		officersResponseYouLimited.items[0].dateOfBirth shouldBe MonthYear(null, null)
		officersResponseYouLimited.items[1].dateOfBirth shouldBe MonthYear(year = 1985, month = 7)
	}

	@Test
	fun `when appointedOn is not null then it should map to the same date`() {
		officersResponseYouLimited.items[1].appointedOn shouldBe "2017-09-02"
	}

	@Test
	fun `when appointedOn is null then it should map to Unknown`() {
		officersResponseYouLimited.items[0].appointedOn shouldBe "Unknown"
	}

	@Test
	fun `when resignedOn is not null then it should map to from to date`() {
		officersResponseYouLimited.items[3].fromToString shouldBe "From 2002-07-02 to 2002-07-02"
	}

	@Test
	fun `when resignedOn is null then it should map to from to date`() {
		officersResponseYouLimited.items[1].fromToString shouldBe "From 2017-09-02"
	}

	//endregion

	//region Person mapping

	private val personsJson = this.loadJson("persons_yorkshire_air_ambulance")
	private val personsResponseDto: PersonsResponseDto = Gson().fromJson(personsJson, PersonsResponseDto::class.java)

	@Test
	fun `when there is an personsResponse json then it is mapped`() {
		val personsResponseYouLimited = companiesHouseMapper.mapPersonsResponse(personsResponseDto)
		personsResponseYouLimited.totalResults shouldBe 5
		personsResponseYouLimited.items[0].name shouldBe "Mr Peter Sunderland"
		personsResponseYouLimited.items[0].dateOfBirth shouldBe MonthYear(year = 1942, month = 3)
	}

	//endregion

}
