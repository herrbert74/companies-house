package com.babestudios.companyinfouk.officers.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.babestudios.base.mvrx.BaseActivity
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.features.OfficersBaseNavigatable
import com.babestudios.companyinfouk.officers.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OfficersActivity : BaseActivity() {

	@Inject
	lateinit var officersNavigator: OfficersBaseNavigatable

	private lateinit var navController: NavController

	private lateinit var companyNumber: String

	@Inject
	lateinit var officersViewModelFactory: OfficersViewModelFactory

	@Suppress("unused")
	private val viewModel: OfficersViewModel by viewModels {
		OfficersViewModel.provideFactory(officersViewModelFactory, intent.getStringExtra(COMPANY_NUMBER).orEmpty())
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		companyNumber = intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		setContentView(R.layout.activity_officers)
		navController = findNavController(R.id.navHostFragmentOfficers)
		officersNavigator.bind(navController)
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
