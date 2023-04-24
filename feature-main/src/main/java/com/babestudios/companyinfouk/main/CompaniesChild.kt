package com.babestudios.companyinfouk.main

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

	internal data class Company(val component: CompanyComp) : CompaniesChild()
	internal data class Favourites(val component: FavouritesComp) : CompaniesChild()
	internal data class Main(val component: MainComp) : CompaniesChild()
	internal data class Map(val component: MapComp) : CompaniesChild()
	internal data class Privacy(val component: PrivacyComp) : CompaniesChild()

	internal data class Charges(val component: ChargesComp) : CompaniesChild()
	internal data class ChargesDetails(val component: ChargeDetailsComp) : CompaniesChild()

	internal data class FilingHistory(val component: FilingHistoryComp) : CompaniesChild()
	internal data class FilingDetails(val component: FilingDetailsComp) : CompaniesChild()

	internal data class Insolvencies(val component: InsolvenciesComp) : CompaniesChild()
	internal data class InsolvencyDetails(val component: InsolvencyDetailsComp) : CompaniesChild()
	internal data class Practitioners(val component: PractitionerDetailsComp) : CompaniesChild()

	internal data class Officers(val component: OfficersComp) : CompaniesChild()
	internal data class OfficerDetails(val component: OfficerDetailsComp) : CompaniesChild()
	internal data class Appointments(val component: AppointmentsComp) : CompaniesChild()

	internal data class Persons(val component: PersonsComp) : CompaniesChild()
	internal data class PersonDetails(val component: PersonDetailsComp) : CompaniesChild()

}
