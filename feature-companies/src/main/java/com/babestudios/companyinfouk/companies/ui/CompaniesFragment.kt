package com.babestudios.companyinfouk.companies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.companies.ui.company.CompanyExecutor
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher

@AndroidEntryPoint
class CompaniesFragment : Fragment() {

	@Inject
	lateinit var companyExecutor: CompanyExecutor

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	private val args: CompaniesFragmentArgs by navArgs()

	private lateinit var companiesRootComponent: CompaniesRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		companiesRootComponent = CompaniesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			mainContext,
			ioContext,
			companiesRepository,
			args.name,
			args.number,
			(findNavController()::popBackStack),
			//(::navigateToMap)
			companyExecutor,
			//favouritesExecutor,
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				CompaniesRootContent(companiesRootComponent)
			}
		}
	}

	//endregion

}
