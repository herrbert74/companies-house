package com.babestudios.companyinfouk.companies

import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.CompaniesBaseNavigatable

class CompaniesNavigation : BaseNavigation(), CompaniesBaseNavigatable {

	override var navController: NavController? = null

	override fun mainToCompany() {
		navController?.navigateSafe(R.id.action_mainFragment_to_companyFragment)
	}

	override fun mainToCompanyPopMain() {
		navController?.navigateSafe(R.id.action_mainFragment_to_companyFragment_pop)
	}

	override fun mainToMapPopMain() {
		navController?.navigateSafe(R.id.action_mainFragment_to_mapFragment_pop)
	}

	override fun mainToFavourites() {
		navController?.navigateSafe(R.id.action_mainFragment_to_favouritesFragment)
	}

	override fun mainToPrivacy() {
		navController?.navigateSafe(R.id.action_mainFragment_to_privacyFragment)
	}

	override fun favouritesToCompany(companyNumber: String, companyName: String) {
		navController?.navigateSafe(R.id.actionToCompany)
	}

	override fun companyToMap() {
		navController?.navigateSafe(R.id.action_companyFragment_to_mapFragment)
	}

}
