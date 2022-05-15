package com.babestudios.companyinfouk.filings

import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.FilingsBaseNavigatable

class FilingsNavigation : BaseNavigation(), FilingsBaseNavigatable {

	override var navController: NavController? = null

	override fun filingsToFilingsDetails() {
		navController?.navigateSafe(R.id.action_filingsFragment_to_filingDetailsFragment)
	}

}
