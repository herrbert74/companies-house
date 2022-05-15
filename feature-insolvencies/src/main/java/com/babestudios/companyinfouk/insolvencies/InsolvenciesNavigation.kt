package com.babestudios.companyinfouk.insolvencies

import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.InsolvenciesBaseNavigatable

class InsolvenciesNavigation : BaseNavigation(), InsolvenciesBaseNavigatable {

	override var navController: NavController? = null

	override fun insolvenciesToInsolvencyDetails() {
		navController?.navigateSafe(R.id.action_insolvenciesFragment_to_insolvencyDetailsFragment)
	}

	override fun insolvencyDetailsToPractitioner() {
		navController?.navigateSafe(R.id.action_insolvencyDetailsFragment_to_practitionerFragment)
	}

}
