package com.babestudios.companyinfouk.data.network

import com.babestudios.companyinfouk.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.data.model.company.CompanyDto
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.data.model.persons.PersonDto
import com.babestudios.companyinfouk.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

internal interface CompaniesHouseApi {
	@GET("search/companies")
	suspend fun searchCompanies(
		@Query("q") searchTerm: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): CompanySearchResult

	@GET("company/{companyNumber}")
	suspend fun getCompany(
		@Path("companyNumber") companyNumber: String,
	): CompanyDto

	@GET("company/{companyNumber}/filing-history")
	suspend fun getFilingHistory(
		@Path("companyNumber") companyNumber: String,
		@Query("category") category: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): FilingHistoryDto

	@GET("company/{companyNumber}/charges")
	suspend fun getCharges(
		@Path("companyNumber") companyNumber: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): ChargesDto

	@GET("company/{companyNumber}/insolvency")
	suspend fun getInsolvency(
		@Path("companyNumber") companyNumber: String,
	): InsolvencyDto

	@GET("company/{companyNumber}/officers")
	@Suppress("LongParameterList")
	suspend fun getOfficers(
		@Path("companyNumber") companyNumber: String,
		@Query("registerView") registerView: String?,
		@Query("registerType") registerType: String?,
		@Query("orderBy") orderBy: String?,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): OfficersResponseDto

	@GET("officers/{officerId}/appointments")
	suspend fun getOfficerAppointments(
		@Path("officerId") officerId: String,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): AppointmentsResponseDto

	@GET("company/{companyNumber}/persons-with-significant-control")
	suspend fun getPersons(
		@Path("companyNumber") companyNumber: String,
		@Query("registerView") registerView: String?,
		@Query("items_per_page") itemsPerPage: String,
		@Query("start_index") startIndex: String,
	): PersonsResponseDto

	@GET("company/{companyNumber}/persons-with-significant-control/individual/{pscId}")
	suspend fun getPersonIndividual(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

	@GET("company/{companyNumber}/persons-with-significant-control/corporate-entity/{pscId}")
	suspend fun getCorporatePerson(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

	@GET("company/{companyNumber}/persons-with-significant-control/corporate-entity/{pscId}")
	suspend fun getLegalPerson(
		@Path("companyNumber") companyNumber: String,
		@Path("pscId") pscId: String,
	): PersonDto

}


