package com.babestudios.companyinfouk.charges

import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.ChargesBaseNavigatable

class ChargesNavigation : BaseNavigation(), ChargesBaseNavigatable {

	override var navController: NavController? = null

	override fun chargesToChargesDetails() {
		navController?.navigateSafe(R.id.action_chargesFragment_to_chargesDetailsFragment)
	}

}
