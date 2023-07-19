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

	fun FilingHistoryDto.mapFilingHistory(): FilingHistory
	fun ChargesDto.mapChargesHistory(): Charges
	fun CompanyDto.mapCompany(): Company
	fun InsolvencyDto.mapInsolvency(): Insolvency
	fun OfficersResponseDto.mapOfficers(): OfficersResponse
	fun AppointmentsResponseDto.mapAppointments(): AppointmentsResponse
	fun PersonsResponseDto.mapPersonsResponse(): PersonsResponse
	fun PersonDto.mapPerson(): Person

}
