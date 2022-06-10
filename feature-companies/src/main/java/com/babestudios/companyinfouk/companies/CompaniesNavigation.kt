package com.babestudios.companyinfouk.companies

import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.companies.ui.main.MainFragmentDirections
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable

class CompaniesNavigation : BaseNavigation(), CompaniesBaseNavigatable {

	override var navController: NavController? = null

	override fun mainToCompany(number: String, name: String) {
		navController?.navigateSafe(MainFragmentDirections.actionToCompany(number, name))
	}

	override fun mainToFavourites() {
		navController?.navigateSafe(R.id.action_mainFragment_to_favouritesFragment)
	}

	override fun mainToPrivacy() {
		navController?.navigateSafe(R.id.action_mainFragment_to_privacyFragment)
	}

}
