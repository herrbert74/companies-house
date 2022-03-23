package com.babestudios.companyinfouk.persons.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.ext.isLazyInitialized
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator
import com.babestudios.companyinfouk.persons.R

class PersonsActivity : BaseActivity() {

	private val comp by lazy {
		DaggerPersonsComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
	}

	private lateinit var personsNavigator: PersonsNavigator

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		setContentView(R.layout.activity_persons)
		navController = findNavController(R.id.navHostFragmentPersons)
		if (::comp.isLazyInitialized) {
			personsNavigator.bind(navController)
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

	fun injectCompaniesHouseRepository(): CompaniesRxRepository {
		return comp.companiesRepository()
	}

	fun injectPersonsNavigator(): PersonsNavigator {
		personsNavigator = comp.navigator()
		if (::navController.isInitialized)
			personsNavigator.bind(navController)
		return personsNavigator
	}
}
