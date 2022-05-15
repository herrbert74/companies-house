package com.babestudios.companyinfouk.insolvencies.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.InsolvenciesBaseNavigatable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class InsolvenciesActivity : BaseActivity() {

	@Inject
	lateinit var companiesRepository: CompaniesRxRepository

	@Inject
	lateinit var insolvenciesNavigator: InsolvenciesBaseNavigatable

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		setContentView(R.layout.activity_insolvencies)
		navController = findNavController(R.id.navHostFragmentInsolvencies)
		insolvenciesNavigator.bind(navController)
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

	fun injectInsolvenciesNavigator(): InsolvenciesBaseNavigatable {
		if (::navController.isInitialized)
			insolvenciesNavigator.bind(navController)
		return insolvenciesNavigator
	}

	fun injectDatesTitleString(): String {
		return resources.getText(R.string.insolvency_dates).toString()
	}

	fun injectPractitionersTitleString(): String {
		return resources.getText(R.string.insolvency_practitioners).toString()
	}
}
