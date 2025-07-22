package com.babestudios.companyinfouk.shared.root

import com.babestudios.companyinfouk.shared.screen.chargedetails.ChargeDetailsComp
import com.babestudios.companyinfouk.shared.screen.charges.ChargesComp
import com.babestudios.companyinfouk.shared.screen.company.CompanyComp
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesComp
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsComp
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComp
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesComp
import com.babestudios.companyinfouk.shared.screen.insolvencydetails.InsolvencyDetailsComp
import com.babestudios.companyinfouk.shared.screen.main.MainComp
import com.babestudios.companyinfouk.shared.screen.map.MapComp
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsComp
import com.babestudios.companyinfouk.shared.screen.officerdetails.OfficerDetailsComp
import com.babestudios.companyinfouk.shared.screen.officers.OfficersComp
import com.babestudios.companyinfouk.shared.screen.persondetails.PersonDetailsComp
import com.babestudios.companyinfouk.shared.screen.persons.PersonsComp
import com.babestudios.companyinfouk.shared.screen.practitionerdetails.PractitionerDetailsComp
import com.babestudios.companyinfouk.shared.screen.privacy.PrivacyComp

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
