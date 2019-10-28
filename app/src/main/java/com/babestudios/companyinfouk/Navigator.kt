package com.babestudios.companyinfouk

import androidx.navigation.Navigator
import com.babestudios.companyinfouk.navigation.base.BaseNavigator
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator

internal class Navigator : BaseNavigator(),
		OfficersNavigator,
		NavigationComponent {

	//region global

	override fun popBackStack() {
		navController?.popBackStack()
	}

	//endregion

	//region features

	override fun provideOfficersNavigation(): OfficersNavigator {
		return this
	}

	//endregion

	//region officers

	override fun officersToOfficerDetails() {
		navController?.navigate(R.id.action_officersFragment_to_officerDetailsFragment)
	}

	override fun officersDetailsToAppointments(extras: Navigator.Extras) {
		navController?.navigate(R.id.action_officerDetailsFragment_to_officerAppointmentFragment, null, null, extras)
	}

	//endregion
}
