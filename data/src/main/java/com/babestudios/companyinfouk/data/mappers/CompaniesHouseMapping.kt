package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.shared.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.shared.data.model.company.CompanyDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.shared.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse

interface CompaniesHouseMapping {

	fun mapFilingHistory(input: FilingHistoryDto): FilingHistory
	fun mapChargesHistory(input: ChargesDto): Charges
	fun mapCompany(input: CompanyDto): Company
	fun mapInsolvency(input: InsolvencyDto): Insolvency
	fun mapOfficers(input: OfficersResponseDto): OfficersResponse
	fun mapAppointments(input: AppointmentsResponseDto): AppointmentsResponse
	fun mapPersonsResponse(input: PersonsResponseDto): PersonsResponse
	fun mapPerson(input: PersonDto): Person

}
