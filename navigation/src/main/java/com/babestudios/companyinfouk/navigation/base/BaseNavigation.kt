package com.babestudios.companyinfouk.navigation.base

import androidx.navigation.NavController

abstract class BaseNavigation : BaseNavigatable {

	override fun bind(navController: NavController) {
		this.navController = navController
	}

	override fun popBackStack() {
		navController?.popBackStack()
	}
}
