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

class CompaniesHouseMapper : CompaniesHouseMapping {

	override fun FilingHistoryDto.mapFilingHistory(): FilingHistory {
		return toFilingHistory()
	}

	override fun ChargesDto.mapChargesHistory(): Charges {
		return this.toCharges()
	}

	override fun CompanyDto.mapCompany(): Company {
		return this.toCompany()
	}

	override fun InsolvencyDto.mapInsolvency(): Insolvency {
		return this.toInsolvency()
	}

	override fun OfficersResponseDto.mapOfficers(): OfficersResponse {
		return this.toOfficersResponse()
	}

	override fun AppointmentsResponseDto.mapAppointments(): AppointmentsResponse {
		return toAppointmentsResponse()
	}

	override fun PersonsResponseDto.mapPersonsResponse(): PersonsResponse {
		return this.toPersonsResponse()
	}

	override fun PersonDto.mapPerson(): Person {
		return this.toPerson()
	}

}
