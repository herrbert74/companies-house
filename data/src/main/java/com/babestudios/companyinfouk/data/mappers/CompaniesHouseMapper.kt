package com.babestudios.companyinfouk.data.mappers

import com.babestudios.companyinfouk.data.local.apilookup.ChargesHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.ConstantsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.FilingHistoryDescriptionsHelperContract
import com.babestudios.companyinfouk.data.local.apilookup.PscHelperContract
import com.babestudios.companyinfouk.shared.data.model.charges.ChargesDto
import com.babestudios.companyinfouk.shared.data.model.company.CompanyDto
import com.babestudios.companyinfouk.shared.data.model.filinghistory.FilingHistoryDto
import com.babestudios.companyinfouk.shared.data.model.insolvency.InsolvencyDto
import com.babestudios.companyinfouk.shared.data.model.officers.AppointmentsResponseDto
import com.babestudios.companyinfouk.shared.data.model.officers.OfficersResponseDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonDto
import com.babestudios.companyinfouk.shared.data.model.persons.PersonsResponseDto
import com.babestudios.companyinfouk.data.utils.StringResourceHelperContract
import com.babestudios.companyinfouk.shared.domain.model.charges.Charges
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistory
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Insolvency
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointmentsResponse
import com.babestudios.companyinfouk.shared.domain.model.officers.OfficersResponse
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.domain.model.persons.PersonsResponse

class CompaniesHouseMapper(
	private val filingHistoryDescriptionsHelper: FilingHistoryDescriptionsHelperContract,
	private val chargesHelper: ChargesHelperContract,
	private val constantsHelper: ConstantsHelperContract,
	private val stringResourceHelper: StringResourceHelperContract,
	private val pscHelper: PscHelperContract,
) : CompaniesHouseMapping {

	override fun mapFilingHistory(input: FilingHistoryDto): FilingHistory {
		return mapFilingHistoryDto(input, filingHistoryDescriptionsHelper)
	}

	override fun mapChargesHistory(input: ChargesDto): Charges {
		return mapChargesDto(input, chargesHelper)
	}

	override fun mapCompany(input: CompanyDto): Company {
		return mapCompanyDto(input, constantsHelper, stringResourceHelper)
	}

	override fun mapInsolvency(input: InsolvencyDto): Insolvency {
		return mapInsolvencyDto(input, constantsHelper)
	}

	override fun mapOfficers(input: OfficersResponseDto): OfficersResponse {
		return mapOfficersResponseDto(input, constantsHelper, stringResourceHelper)
	}

	override fun mapAppointments(input: AppointmentsResponseDto): AppointmentsResponse {
		return mapAppointmentsResponseDto(input, constantsHelper)
	}

	override fun mapPersonsResponse(input: PersonsResponseDto): PersonsResponse {
		return mapPersonsResponseDto(input, pscHelper)
	}

	override fun mapPerson(input: PersonDto): Person {
		return mapPersonDto(input, pscHelper)
	}

}
