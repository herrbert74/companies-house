package com.babestudios.companyinfouk.filings.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.FilingsBaseNavigatable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FilingsActivity : BaseActivity() {

	@Inject
	lateinit var companiesRepository: CompaniesRxRepository

	@Inject
	lateinit var filingsNavigator: FilingsBaseNavigatable

	private lateinit var navController: NavController

	lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER) ?: ""
		setContentView(R.layout.activity_filings)

		navController = findNavController(R.id.navHostFragmentFilings)
			filingsNavigator.bind(navController)
	}

	override fun onBackPressed() {
		if (onBackPressedDispatcher.hasEnabledCallbacks()) {
			onBackPressedDispatcher.onBackPressed()
		} else {
			super.finish()
			overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
		}
	}

	fun provideCompanyNumber(): String {
		return if (::companyNumber.isInitialized) companyNumber else ""
	}

	fun injectCompaniesHouseRepository(): CompaniesRxRepository {
		return companiesRepository
	}

	fun injectFilingsNavigator(): FilingsBaseNavigatable {
		if (::navController.isInitialized) {
			filingsNavigator.bind(navController)
		}
		return filingsNavigator
	}
}
