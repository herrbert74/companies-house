package com.babestudios.companyinfouk.main

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.babestudios.companyinfouk.charges.ui.charges.ChargesScreen
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsListScreen
import com.babestudios.companyinfouk.companies.ui.company.CompanyScreen
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesScreen
import com.babestudios.companyinfouk.companies.ui.main.MainScreen
import com.babestudios.companyinfouk.companies.ui.map.MapScreen
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyScreen
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsScreen
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryScreen
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsListScreen
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesScreen
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsScreen
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsScreen
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsScreen
import com.babestudios.companyinfouk.officers.ui.officers.OfficersScreen
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsScreen
import com.babestudios.companyinfouk.persons.ui.persons.PersonsScreen
import com.babestudios.companyinfouk.shared.root.CompaniesChild
import com.babestudios.companyinfouk.shared.root.CompaniesRootComp

@Composable
internal fun CompaniesRootContent(component: CompaniesRootComp) {

	val stack = component.childStackValue

	CompaniesTheme {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is CompaniesChild.Company -> CompanyScreen(child.component)
				is CompaniesChild.Favourites -> FavouritesScreen(child.component)
				is CompaniesChild.Main -> MainScreen(child.component)
				is CompaniesChild.Map -> MapScreen(child.component)
				is CompaniesChild.Privacy -> PrivacyScreen(child.component)
				is CompaniesChild.Charges -> ChargesScreen(child.component)
				is CompaniesChild.ChargesDetails -> ChargeDetailsListScreen(child.component)
				is CompaniesChild.FilingHistory -> FilingHistoryScreen(child.component)
				is CompaniesChild.FilingDetails -> FilingDetailsScreen(child.component)
				is CompaniesChild.Insolvencies -> InsolvenciesScreen(child.component)
				is CompaniesChild.InsolvencyDetails -> InsolvencyDetailsListScreen(child.component)
				is CompaniesChild.Practitioners -> PractitionerDetailsScreen(child.component)
				is CompaniesChild.Officers -> OfficersScreen(child.component)
				is CompaniesChild.OfficerDetails -> OfficerDetailsScreen(child.component)
				is CompaniesChild.Appointments -> AppointmentsScreen(child.component)
				is CompaniesChild.Persons -> PersonsScreen(child.component)
				is CompaniesChild.PersonDetails -> PersonDetailsScreen(child.component)
			}
		}
	}

}
