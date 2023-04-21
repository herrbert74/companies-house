package com.babestudios.companyinfouk.data.network

import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface CompaniesHouseService {
	@GET(BuildConfig.COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT)
	suspend fun searchCompanies(
		@Query("q") searchTerm: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): CompanySearchResult

	@GET(BuildConfig.COMPANIES_HOUSE_GET_COMPANY_ENDPOINT)
	suspend fun getCompany(
		@Path("companyNumber") companyNumber: String,
	): CompanyDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_FILING_HISTORY_ENDPOINT)
	suspend fun getFilingHistory(
		@Path("companyNumber") companyNumber: String,
		@Query("category") category: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): FilingHistoryDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_CHARGES_ENDPOINT)
	suspend fun getCharges(
		@Path("companyNumber") companyNumber: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): ChargesDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_INSOLVENCY_ENDPOINT)
	suspend fun getInsolvency(
		@Path("companyNumber") companyNumber: String,
	): InsolvencyDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_OFFICERS_ENDPOINT)
	@Suppress("LongParameterList")
	suspend fun getOfficers(
		@Path("companyNumber") companyNumber: String,
		@Query("registerView") registerView: String?,
		@Query("registerType") registerType: String?,
		@Query("orderBy") orderBy: String?,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): OfficersResponseDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_OFFICERS_APPOINTMENTS_ENDPOINT)
	suspend fun getOfficerAppointments(
		@Path("officerId") officerId: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): AppointmentsResponseDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_ENDPOINT)
	suspend fun getPersons(
		@Path("companyNumber") companyNumber: String,
		@Query("registerView") registerView: String?,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): PersonsResponseDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_INDIVIDUAL_ENDPOINT)
	suspend fun getPersonIndividual(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_CORPORATE_ENDPOINT)
	suspend fun getCorporatePerson(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_CORPORATE_ENDPOINT)
	suspend fun getLegalPerson(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

}


