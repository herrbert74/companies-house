package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.navigation.COMPANY_NAME
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_ADDRESS
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_NAME
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator

class CompaniesActivity : BaseActivity() {

	private val comp by lazy {
		DaggerCompaniesComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var companiesNavigator: CompaniesNavigator

	private lateinit var navController: NavController

	private lateinit var companyNumber: String
	private lateinit var companyName: String
	private var individualName: String? = null
	private var individualAddress: Address? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.extras?.getString(COMPANY_NUMBER).orEmpty()
		companyName = intent.extras?.getString(COMPANY_NAME).orEmpty()
		individualName = intent.extras?.getString(INDIVIDUAL_NAME)
		individualAddress = intent.extras?.getParcelable(INDIVIDUAL_ADDRESS)
		setContentView(R.layout.activity_companies)
		navController = findNavController(R.id.navHostFragmentCompanies)
		if (::comp.isLazyInitialized) {
			companiesNavigator.bind(navController)
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

	fun injectCompaniesHouseRepository(): CompaniesRepository {
		return comp.companiesRepository()
	}

	fun injectCompaniesNavigator(): CompaniesNavigator {
		companiesNavigator = comp.navigator()
		if (::navController.isInitialized) {
			companiesNavigator.bind(navController)
			if (companyNumber.isNotEmpty())
				companiesNavigator.mainToCompanyPopMain()
			else if(individualAddress != null)
				companiesNavigator.mainToMapPopMain()
		}
		return companiesNavigator
	}

	fun injectRecentSearchesString(): String {
		return getString(R.string.recent_searches)
	}

	fun provideCompanyNumber(): String {
		return if (::companyNumber.isInitialized) companyNumber else ""
	}

	fun provideCompanyName(): String {
		return if (::companyName.isInitialized) companyName else ""
	}

	fun provideIndividualName(): String? {
		return individualName
	}

	fun provideIndividualAddress(): Address? {
		return individualAddress
	}
}
