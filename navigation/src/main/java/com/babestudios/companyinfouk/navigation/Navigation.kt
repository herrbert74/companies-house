package com.babestudios.companyinfouk.navigation

import androidx.navigation.NavController
import com.babestudios.companyinfouk.navigation.MainNavGraphDirections
import com.babestudios.companyinfouk.navigation.NavigationFlow

class Navigation {

	lateinit var navController: NavController

	fun bind(navController: NavController) {
		this.navController = navController
	}

	fun popBackStack() {
		navController.popBackStack()
	}

	fun navigateToFlow(navigationFlow: NavigationFlow) = when (navigationFlow) {
		is NavigationFlow.ChargesFlow ->
			navController.navigate(MainNavGraphDirections.actionGlobalChargesFlow(navigationFlow.selectedCompanyId))

		is NavigationFlow.FilingsFlow ->
			navController.navigate(MainNavGraphDirections.actionGlobalFilingsFlow(navigationFlow.selectedCompanyId))

		is NavigationFlow.InsolvenciesFlow -> navController.navigate(
			MainNavGraphDirections.actionGlobalInsolvenciesFlow(navigationFlow.selectedCompanyId)
		)

		is NavigationFlow.OfficersFlow ->
			navController.navigate(MainNavGraphDirections.actionGlobalOfficersFlow(navigationFlow.selectedCompanyId))

		is NavigationFlow.PersonsFlow ->
			navController.navigate(MainNavGraphDirections.actionGlobalPersonsFlow(navigationFlow.selectedCompanyId))
	}
}
