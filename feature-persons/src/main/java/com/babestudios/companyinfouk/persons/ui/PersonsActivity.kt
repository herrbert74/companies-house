package com.babestudios.companyinfouk.persons.ui

import android.os.Bundle
import androidx.activity.viewModels
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

	@Inject
	lateinit var personsViewModelFactory: PersonsViewModelFactory

	@Suppress("unused")
	private val viewModel: PersonsViewModel by viewModels {
		PersonsViewModel.provideFactory(personsViewModelFactory, intent.getStringExtra(COMPANY_NUMBER).orEmpty())
	}
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

}
