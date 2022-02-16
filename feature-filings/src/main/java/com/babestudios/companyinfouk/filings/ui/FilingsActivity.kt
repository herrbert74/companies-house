package com.babestudios.companyinfouk.filings.ui

import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator

class FilingsActivity : BaseActivity() {

	private val comp by lazy {
		DaggerFilingsComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var filingsNavigator: FilingsNavigator

	private lateinit var navController: NavController

	lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER) ?: ""
		setContentView(R.layout.activity_filings)

		navController = findNavController(R.id.navHostFragmentFilings)
		if (::comp.isLazyInitialized) {
			filingsNavigator.bind(navController)
		}
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

	fun injectCompaniesHouseRepository(): CompaniesRepositoryContract {
		return comp.companiesRepository()
	}

	fun injectFilingsNavigator(): FilingsNavigator {
		filingsNavigator = comp.navigator()
		if (::navController.isInitialized) {
			filingsNavigator.bind(navController)
		}
		return filingsNavigator
	}
}
