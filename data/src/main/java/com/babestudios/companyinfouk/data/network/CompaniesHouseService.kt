package com.babestudios.companyinfouk.data.network

import com.babestudios.companyinfouk.data.BuildConfig
import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CompaniesHouseService {
	@GET(BuildConfig.COMPANIES_HOUSE_SEARCH_COMPANIES_ENDPOINT)
	fun searchCompanies(
			@Query("q") searchTerm: String,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<CompanySearchResult>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_COMPANY_ENDPOINT)
	fun getCompany(
			@Path("companyNumber") companyNumber: String
	): Single<CompanyDto>


	@GET(BuildConfig.COMPANIES_HOUSE_GET_FILING_HISTORY_ENDPOINT)
	fun getFilingHistory(
			@Path("companyNumber") companyNumber: String,
			@Query("category") category: String,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<FilingHistoryDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_CHARGES_ENDPOINT)
	fun getCharges(
			@Path("companyNumber") companyNumber: String,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<ChargesDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_INSOLVENCY_ENDPOINT)
	fun getInsolvency(
			@Path("companyNumber") companyNumber: String
	): Single<InsolvencyDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_OFFICERS_ENDPOINT)
	@Suppress("LongParameterList")
	fun getOfficers(
			@Path("companyNumber") companyNumber: String,
			@Query("registerView") registerView: String?,
			@Query("registerType") registerType: String?,
			@Query("orderBy") orderBy: String?,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<OfficersResponseDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_OFFICERS_APPOINTMENTS_ENDPOINT)
	fun getOfficerAppointments(
			@Path("officerId") officerId: String,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<AppointmentsResponseDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_ENDPOINT)
	fun getPersons(
			@Path("companyNumber") companyNumber: String,
			@Query("registerView") registerView: String?,
			@Query("items_per_page") itemsPerPage: String,
			@Query("start_index") startIndex: String
	): Single<PersonsResponseDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_INDIVIDUAL_ENDPOINT)
	fun getPersonIndividual(
			@Path("companyNumber") companyNumber: String,
			@Path("pscId") pscId: String,
	): Single<PersonDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_CORPORATE_ENDPOINT)
	fun getCorporatePerson(
			@Path("companyNumber") companyNumber: String,
			@Path("pscId") pscId: String,
	): Single<PersonDto>

	@GET(BuildConfig.COMPANIES_HOUSE_GET_PERSONS_CORPORATE_ENDPOINT)
	fun getLegalPerson(
			@Path("companyNumber") companyNumber: String,
			@Path("pscId") pscId: String,
	): Single<PersonDto>

}


