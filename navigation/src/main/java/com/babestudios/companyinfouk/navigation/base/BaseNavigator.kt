package com.babestudios.companyinfouk.navigation.base

import androidx.navigation.NavController

interface Navigator {
	fun bind(navController: NavController)
	fun unbind()
	fun popBackStack()
}

abstract class BaseNavigator : Navigator {

	protected var navController: NavController? = null

	override fun bind(navController: NavController) {
		this.navController = navController
	}

	override fun unbind() {
		navController = null
	}
}