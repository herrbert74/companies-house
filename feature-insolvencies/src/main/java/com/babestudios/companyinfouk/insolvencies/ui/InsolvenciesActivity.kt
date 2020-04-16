package com.babestudios.companyinfouk.insolvencies.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator

class InsolvenciesActivity : BaseActivity() {

	private val comp by lazy {
		DaggerInsolvenciesComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var insolvenciesNavigator: InsolvenciesNavigator

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_insolvencies)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		navController = findNavController(R.id.navHostFragmentInsolvencies)
		if (::comp.isLazyInitialized) {
			insolvenciesNavigator.bind(navController)
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

	fun injectInsolvenciesNavigator(): InsolvenciesNavigator {
		insolvenciesNavigator = comp.navigator()
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
