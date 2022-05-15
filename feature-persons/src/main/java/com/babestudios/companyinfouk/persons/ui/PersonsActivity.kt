package com.babestudios.companyinfouk.persons.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.domain.api.CompaniesRxRepository
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.PersonsBaseNavigatable
import com.babestudios.companyinfouk.persons.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PersonsActivity : BaseActivity() {

	@Inject
	lateinit var companiesRepository: CompaniesRxRepository

	@Inject
	lateinit var personsNavigator: PersonsBaseNavigatable

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		setContentView(R.layout.activity_persons)
		navController = findNavController(R.id.navHostFragmentPersons)
		personsNavigator.bind(navController)
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

	fun injectPersonsNavigator(): PersonsBaseNavigatable {
		if (::navController.isInitialized)
			personsNavigator.bind(navController)
		return personsNavigator
	}
}
