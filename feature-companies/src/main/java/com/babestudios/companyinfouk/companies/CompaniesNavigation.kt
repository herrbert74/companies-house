package com.babestudios.companyinfouk.companies

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
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
		navController?.navigateSafe(R.id.action_favouritesFragment_to_companyFragment)
	}

	override fun companyToMap() {
		navController?.navigateSafe(R.id.action_companyFragment_to_mapFragment)
	}

	override fun companyToCharges(companyNumber: String) {
		val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
		navController?.navigateSafe(R.id.action_companyFragment_to_chargesActivity, bundle)
	}

	override fun companyToFilings(companyNumber: String) {
		val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
		navController?.navigateSafe(R.id.action_companyFragment_to_filingsActivity, bundle)
	}

	override fun companyToInsolvencies(companyNumber: String) {
		val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
		navController?.navigateSafe(R.id.action_companyFragment_to_insolvenciesActivity, bundle)
	}

}
