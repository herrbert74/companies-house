package com.babestudios.companyinfouk.mock

import com.babestudios.companyinfouk.loadJson
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointedTo
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficerLinks
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficerRelatedLinks
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResult
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.github.michaelbull.result.Ok
import com.google.gson.GsonBuilder
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.eq
import dev.mokkery.mock
import kotlinx.datetime.YearMonth

fun mockCompaniesRepository(): CompaniesRepository {
	val gson = GsonBuilder()
		.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
		.create()

	val mockCompaniesRepository = mock<CompaniesRepository>()

	mockCompaniesRepository.mockWithFavourites()

	every {
		mockCompaniesRepository.logAppOpen()
	} returns Unit

	every {
		mockCompaniesRepository.logScreenView(any())
	} returns Unit

	every {
		mockCompaniesRepository.logSearch(any())
	} returns Unit

	everySuspend {
		mockCompaniesRepository.recentSearches()
	} returns favourites

	everySuspend {
		mockCompaniesRepository.getCompany("1")
	} returns Company(companyName = "Acme Painting", companyNumber = "1")

	everySuspend {
		mockCompaniesRepository.getCompany("04475590")
	} returns Company(companyName = "You Limited", companyNumber = "04475590")

	everySuspend {
		mockCompaniesRepository.isFavourite(favourites[0])
	} returns true

	everySuspend {
		mockCompaniesRepository.isFavourite(favourites[1])
	} returns false

	everySuspend {
		mockCompaniesRepository.removeFavourite(any())
	} returns Unit

	val jsonSearchResultForYou = gson.loadJson("search_result_you")

	//That does not work
//	val jsonSearchResultForYou = readResource("./src/androidTest/resources/search_result_you.json")
	val companySearchItemForYou = gson.fromJson(jsonSearchResultForYou, CompanySearchResult::class.java)

	everySuspend {
		mockCompaniesRepository.searchCompanies(eq("you"), any())
	} returns companySearchItemForYou

	everySuspend {
		mockCompaniesRepository.searchCompanies(eq("xyzabc"), any())
	} returns CompanySearchResult()

	everySuspend {
		mockCompaniesRepository.searchCompanies(eq(""), any())
	} returns CompanySearchResult()

	everySuspend {
		mockCompaniesRepository.getOfficers(eq("1"), any())
	} returns Ok(officersResponseYouLimited)

	everySuspend {
		mockCompaniesRepository.getOfficerAppointments(eq("M0nRpSZPlPTwBusql3sNK6Efzr8"), any())
	} returns Ok(officersAppointmentsYouLimited)

	return mockCompaniesRepository

}

fun CompaniesRepository.mockWithEmptyFavourites() = apply { everySuspend { favourites() } returns emptyList() }

fun CompaniesRepository.mockWithFavourites() = apply { everySuspend { favourites() } returns favourites }

private val favourites = listOf(
	SearchHistoryItem(
		"Acme Painting",
		"1",
		111L
	),
	SearchHistoryItem(
		"You Limited",
		"04475590",
		111L
	)
)

private val officersResponseYouLimited: OfficersResponse = OfficersResponse(
	totalResults = 7, items = listOf(
		Officer(
			appointedOn = "2002-07-02",
			links = OfficerLinks(
				officer =
					OfficerRelatedLinks(
						appointments = "/officers/M0nRpSZPlPTwBusql3sNK6Efzr8/appointments"
					)
			), name = "STEVENSON, Elizabeth Mary", officerRole = "Secretary", dateOfBirth = YearMonth(
				year = 0,
				month = 1
			),
			occupation = "Director", countryOfResidence = "Unknown", nationality = "British", resignedOn = null,
			fromToString = "From 2002-07-02", appointmentsId = "M0nRpSZPlPTwBusql3sNK6Efzr8"
		),
		Officer(
			address = Address(
				addressLine1 = "1 Harewood Street", addressLine2 = null, country = "England", locality = "Leeds",
				postalCode = "LS2 7AD", region = "West Yorkshire"
			), appointedOn = "2017-09-02",
			links = OfficerLinks(
				officer = OfficerRelatedLinks(
					appointments = "/officers/5SU_9GfEsP_Z_H7rb1fNFmEZvKs/appointments"
				)
			), name = "STEVENSON, Benjamin Thomas",
			officerRole = "Director", dateOfBirth = YearMonth(year = 1985, month = 7),
			occupation = "Company Director",
			countryOfResidence = "England", nationality = "British", resignedOn = null,
			fromToString = "From 2017-09-02",
			appointmentsId = "5SU_9GfEsP_Z_H7rb1fNFmEZvKs"
		),
		Officer(
			address = Address(
				addressLine1 = "Geeston Road",
				addressLine2 = "Ketton", country = "England", locality = "Stamford", postalCode = "PE9 3RH",
				region = "Lincolnshire"
			),
			appointedOn = "2002-07-02",
			links = OfficerLinks(
				officer = OfficerRelatedLinks
					(appointments = "/officers/M0nRpSZPlPTwBusql3sNK6Efzr8/appointments")
			), name = "STEVENSON, Elizabeth Mary", officerRole = "Director",
			dateOfBirth = YearMonth(year = 1957, month = 3), occupation = "Director",
			countryOfResidence = "England", nationality = "British", resignedOn = null,
			fromToString = "From 2002-07-02", appointmentsId = "M0nRpSZPlPTwBusql3sNK6Efzr8"
		),
		Officer(
			address = Address(
				addressLine1 = "84 Temple Chambers", addressLine2 = "Temple Avenue", country = "Unknown",
				locality = "London", postalCode = "EC4Y 0HP", region = ""
			), appointedOn = "2002-07-02",
			links = OfficerLinks(
				officer = OfficerRelatedLinks(appointments = "/officers/yijMAlD-g9qcqcbX0w6AFDbcWtQ/appointments")
			), name = "LONDON LAW SECRETARIAL LIMITED", officerRole = "Nominee Secretary",
			dateOfBirth = YearMonth(year = 0, month = 1), occupation = "Unknown",
			countryOfResidence = "Unknown", nationality = "Unknown", resignedOn = "2002-07-02",
			fromToString = "From 2002-07-02 to 2002-07-02", appointmentsId = "yijMAlD-g9qcqcbX0w6AFDbcWtQ"
		),
		Officer(
			address = Address(
				addressLine1 = "84 Temple Chambers", addressLine2 = "Temple Avenue", country = "", locality = "London",
				postalCode = "EC4Y 0HP", region = ""
			), appointedOn = "2002-07-02",
			links = OfficerLinks(
				officer = OfficerRelatedLinks(appointments = "/officers/It1fdjIyCDqgLpk3nYKsWymv32U/appointments")
			), name = "LONDON LAW SERVICES LIMITED", officerRole = "Nominee Director",
			dateOfBirth = YearMonth(year = 0, month = 1), occupation = "Unknown",
			countryOfResidence = "Unknown", nationality = "Unknown", resignedOn = "2002-07-02",
			fromToString = "From 2002-07-02 to 2002-07-02", appointmentsId = "It1fdjIyCDqgLpk3nYKsWymv32U"
		),
		Officer(
			address = Address(
				addressLine1 = "Edmonds Drive", addressLine2 = "Ketton", country = "England", locality = "Stamford",
				postalCode = "PE9 3TH", region = "Lincolnshire"
			), appointedOn = "2012-11-09",
			links = OfficerLinks(
				officer = OfficerRelatedLinks(appointments = "/officers/mqkD15s6orcvxGCwF0zIaU7bw1I/appointments")
			), name = "STEVENSON, Benjamin Thomas", officerRole = "Director",
			dateOfBirth = YearMonth(year = 1985, month = 6), occupation = "Company Director",
			countryOfResidence = "England", nationality = "British", resignedOn = "2017-09-01",
			fromToString = "From 2012-11-09 to 2017-09-01", appointmentsId = "mqkD15s6orcvxGCwF0zIaU7bw1I"
		),
		Officer(
			address = Address(
				addressLine1 = "Huckleberry House", addressLine2 = "4a Edmunds Drive", country = "",
				locality = "Ketton", postalCode = "PE9 3TH", region = "Lincolnshire"
			), appointedOn = "2002-07-02",
			links = OfficerLinks(
				officer = OfficerRelatedLinks(appointments = "/officers/w01KCApAVX3NeT_TR6yliDQ44lU/appointments")
			), name = "STEVENSON, Leslie Alan", officerRole = "Director",
			dateOfBirth = YearMonth(year = 1950, month = 4), occupation = "Director",
			countryOfResidence = "Unknown", nationality = "British", resignedOn = "2012-08-28",
			fromToString = "From 2002-07-02 to 2012-08-28", appointmentsId = "w01KCApAVX3NeT_TR6yliDQ44lU"
		)
	)
)

private val appointedToYouLimited = AppointedTo(
	companyName = "YOU LIMITED",
	companyNumber = "04475590",
	companyStatus = "active"
)

private val officersAppointmentsYouLimited: AppointmentsResponse = AppointmentsResponse(
	dateOfBirth = YearMonth(year = 1957, month = 3), items = listOf(
		Appointment(
			address = Address(
				addressLine1 = "Geeston Road", addressLine2 = "Ketton", country = "England", locality = "Stamford",
				postalCode = "PE9 3RH", region = "Lincolnshire"
			), appointedOn = "2002-07-02",
			appointedTo = appointedToYouLimited, countryOfResidence = null, name = "Elizabeth Mary STEVENSON",
			nationality = "British", occupation = "Director", officerRole = "Secretary", resignedOn = null
		),
		Appointment(
			address = Address(
				addressLine1 = "Geeston Road", addressLine2 = "Ketton", country = "England",
				locality = "Stamford", postalCode = "PE9 3RH", region = "Lincolnshire"
			), appointedOn = "2002-07-02",
			appointedTo = appointedToYouLimited, countryOfResidence = "England", name = "Elizabeth Mary STEVENSON",
			nationality = "British", occupation = "Director", officerRole = "Director", resignedOn = null
		)
	),
	name = "Elizabeth Mary STEVENSON", totalResults = 2
)
