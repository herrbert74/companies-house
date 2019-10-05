package com.babestudios.companyinfouk

import com.babestudios.companyinfouk.navigation.base.BaseNavigator
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator

internal class Navigator : BaseNavigator(),
		OfficersNavigator,
		NavigationComponent {

	override fun provideOfficersNavigation(): OfficersNavigator {
		return this
	}

	override fun officersToOfficerDetails() {
		navController?.navigate(R.id.action_officersFragment_to_officerDetailsFragment)
	}
}
