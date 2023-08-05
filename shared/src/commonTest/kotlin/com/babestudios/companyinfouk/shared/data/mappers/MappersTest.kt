package com.babestudios.companyinfouk.shared.data.mappers

import com.babestudios.base.kotlin.io.readCommonResource
import com.babestudios.companyinfouk.shared.data.mapper.formatFilingHistoryDescriptionDto
import com.babestudios.companyinfouk.shared.data.mapper.toAppointmentsResponse
import com.babestudios.companyinfouk.shared.data.mapper.toCharges
import com.babestudios.companyinfouk.shared.data.mapper.toCompany
import com.babestudios.companyinfouk.shared.data.mapper.toFilingHistory
import com.babestudios.companyinfouk.shared.data.mapper.toInsolvency
import com.babestudios.companyinfouk.shared.data.mapper.toOfficersResponse
import com.babestudios.companyinfouk.shared.data.mapper.toPersonsResponse
import com.babestudios.companyinfouk.shared.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.shared.data.model.company.CompanyDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.DescriptionValuesDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.shared.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.shared.domain.model.common.MonthYear
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.serialization.json.Json

class MappersTest {

	private lateinit var officersResponseYouLimited: OfficersResponse

	private var json = Json {
		isLenient = true
		ignoreUnknownKeys = true
	}

	@BeforeTest
	fun setup() {

		officersResponseYouLimited = officersResponseDto.toOfficersResponse()

	}

	//region Appointments mapping

	private val appointmentsJson = readCommonResource("appointments_allan_david_horley.json")
	private val appointmentsResponseDto = json.decodeFromString<AppointmentsResponseDto>(appointmentsJson)

	@Test
	fun `when there is an appointment json then it is mapped`() {
		val appointmentsResponseAllanDavidHorley = appointmentsResponseDto.toAppointmentsResponse()
		appointmentsResponseAllanDavidHorley.totalResults shouldBe 3
		appointmentsResponseAllanDavidHorley.items[0].name shouldBe "Allan David HORLEY"
	}

	//endregion

	//region Charges mapping

	@Test
	fun `when there is a charges json then it is mapped`() {
		val jsonString = readCommonResource("charges_pfb_hire.json")
		val chargesDto = json.decodeFromString<ChargesDto>(jsonString)
		val chargesPfbHire = chargesDto.toCharges()
		chargesPfbHire.totalCount shouldBe 9
		chargesPfbHire.items[0].personsEntitled shouldContain "Art Share"
	}

	//endregion

	//region Company mapping

	private val companyJson = readCommonResource("company_candour.json")
	private val companyDto = json.decodeFromString<CompanyDto>(companyJson)

	@Test
	fun `when there is a company json then it is mapped`() {
		val companyCandour = companyDto.toCompany()
		companyCandour.natureOfBusiness shouldBe "68100 Buying and selling of own real estate"
		companyCandour.lastAccountsMadeUpTo shouldBe "Last account made up to 31 Mar 2019"
		companyCandour.companyName shouldBe "CANDOUR GROUP LIMITED"
		companyCandour.registeredOfficeAddress.addressLine1 shouldBe "71 New Dover Road"
	}

	@Test
	fun `when addressline2 is null then it is mapped to null`() {
		val companyCandour = companyDto.toCompany()
		companyCandour.registeredOfficeAddress.addressLine2 shouldBe null
	}

	@Test
	fun `when address region is null then it is mapped to null`() {
		val companyCandour = companyDto.toCompany()
		companyCandour.registeredOfficeAddress.region shouldBe null
	}

	//endregion

	//region Full Filing History map

	@Test
	fun `when there is a filing history json then it is mapped`() {
		val jsonString = readCommonResource("filing_pfb_hire.json")
		val filingHistoryDto = json.decodeFromString<FilingHistoryDto>(jsonString)
		val filingHistoryPfbHire = filingHistoryDto.toFilingHistory()
		filingHistoryPfbHire.totalCount shouldBe 47

		//This one should (for now) unused 'capital' field removed
		filingHistoryPfbHire.items[19].description shouldBe
			"**Statement of capital following an allotment of shares** on 2014-07-31"

		filingHistoryPfbHire.filingHistoryStatus shouldBe "filing-history-available"
	}

	@Test
	fun `when there is a filing history with change of name then it is mapped`() {
		val jsonString = readCommonResource("filing_tell_aldi.json")
		val filingHistoryDto = json.decodeFromString<FilingHistoryDto>(jsonString)
		val filingHistoryWithNameChange = filingHistoryDto.toFilingHistory()
		filingHistoryWithNameChange.totalCount shouldBe 3

		//This one should (for now) unused 'capital' field removed
		filingHistoryWithNameChange.items[0].description shouldBe
			"**Certificate of change of name**"

		filingHistoryWithNameChange.filingHistoryStatus shouldBe "filing-history-available"
	}

	//endregion

	//region format filing description

	@Test
	fun `when there is a place holder in filing history item then it is mapped`() {
		val description = "test {replace}"
		val descriptionValues = DescriptionValuesDto(mapOf("replace" to ("value")))
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "test value"
	}

	@Test
	fun `when there are three place holders in filing history item then it is mapped`() {
		val description = "**Registered office address changed** from {old_address} to {new_address} on {change_date}"
		val descriptionValues = DescriptionValuesDto(
			mapOf(
				"new_address" to "20-22 Wenlock Road London N1 7GU",
				"old_address" to "99 Evesham Road London N11 2RR England",
				"change_date" to "2020-07-08"
			)
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
		val descriptionValues = DescriptionValuesDto(
			mapOf(
				"description" to "Ad 01/01/08\\gbp si 10@1=10\\gbp ic " +
					"100/110\\"
			)
		)
		val result = formatFilingHistoryDescriptionDto(description, descriptionValues)
		result shouldBe "Ad 01/01/08\\gbp si 10@1=10\\gbp ic 100/110\\"
	}

	//endregion

	//region Insolvency mapping

	@Test
	fun `when there is an insolvency json then it is mapped`() {
		val jsonString = readCommonResource("insolvency_london_airways.json")
		val insolvencyDto = json.decodeFromString<InsolvencyDto>(jsonString)
		val insolvencyLondonAirways = insolvencyDto.toInsolvency()
		insolvencyLondonAirways.cases[0].dates[0].date shouldBe "1995-01-18"
		insolvencyLondonAirways.cases[0].practitioners[0].name shouldBe "Alan Redvers Price"
	}

	//endregion

	//region Officer mapping

	private val officersJson = readCommonResource("officers_you_limited.json")
	private val officersResponseDto = json.decodeFromString<OfficersResponseDto>(officersJson)

	@Test
	fun `when there is an officer json then it is mapped`() {
		officersResponseYouLimited.totalResults shouldBe 7
		officersResponseYouLimited.items[0].name shouldBe "STEVENSON, Elizabeth Mary"
		officersResponseYouLimited.items[0].dateOfBirth shouldBe MonthYear(null, null)
		officersResponseYouLimited.items[1].dateOfBirth shouldBe MonthYear(year = 1985, month = 7)
		officersResponseYouLimited.items[0].appointmentsId shouldBe "M0nRpSZPlPTwBusql3sNK6Efzr8"
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

	private val personsJson = readCommonResource("persons_yorkshire_air_ambulance.json")
	private val personsResponseDto = json.decodeFromString<PersonsResponseDto>(personsJson)

	@Test
	fun `when there is an personsResponse json then it is mapped`() {
		val personsResponseYouLimited = personsResponseDto.toPersonsResponse()
		personsResponseYouLimited.totalResults shouldBe 5
		personsResponseYouLimited.items[0].name shouldBe "Mr Peter Sunderland"
		personsResponseYouLimited.items[0].dateOfBirth shouldBe MonthYear(year = 1942, month = 3)
	}

	//endregion

}
