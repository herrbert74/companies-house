package com.babestudios.companyinfo.navigation.base

import androidx.navigation.NavController

interface Navigator {
	fun bind(navController: NavController)
	fun unbind()
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