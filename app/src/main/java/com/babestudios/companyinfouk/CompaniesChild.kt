package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.charges.ui.charges.ChargesComp
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsComp
import com.babestudios.companyinfouk.companies.ui.company.CompanyComp
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComp
import com.babestudios.companyinfouk.companies.ui.main.MainComp
import com.babestudios.companyinfouk.companies.ui.map.MapComp
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyComp
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsComp
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryComp
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsComp
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesComp
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsComp
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsComp
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsComp
import com.babestudios.companyinfouk.officers.ui.officers.OfficersComp
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsComp
import com.babestudios.companyinfouk.persons.ui.persons.PersonsComp

sealed class CompaniesChild {

	data class Company(val component: CompanyComp) : CompaniesChild()
	data class Favourites(val component: FavouritesComp) : CompaniesChild()
	data class Main(val component: MainComp) : CompaniesChild()
	data class Map(val component: MapComp) : CompaniesChild()
	data class Privacy(val component: PrivacyComp) : CompaniesChild()

	data class Charges(val component: ChargesComp) : CompaniesChild()
	data class ChargesDetails(val component: ChargeDetailsComp) : CompaniesChild()

	data class FilingHistory(val component: FilingHistoryComp) : CompaniesChild()
	data class FilingDetails(val component: FilingDetailsComp) : CompaniesChild()

	data class Insolvencies(val component: InsolvenciesComp) : CompaniesChild()
	data class InsolvencyDetails(val component: InsolvencyDetailsComp) : CompaniesChild()
	data class Practitioners(val component: PractitionerDetailsComp) : CompaniesChild()

	data class Officers(val component: OfficersComp) : CompaniesChild()
	data class OfficerDetails(val component: OfficerDetailsComp) : CompaniesChild()
	data class Appointments(val component: AppointmentsComp) : CompaniesChild()

	data class Persons(val component: PersonsComp) : CompaniesChild()
	data class PersonDetails(val component: PersonDetailsComp) : CompaniesChild()

}