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
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class CompaniesFragment : Fragment() {

	@Inject
	lateinit var mainExecutor: MainExecutor

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

		val startConfiguration: Configuration = when {
			args.address.isNotEmpty() -> Configuration.Map(args.name ?: "", args.address)
			args.number != null -> Configuration.Company(args.number ?:"", args.name ?: "", Configuration.Main)
			else -> Configuration.Main
		}

		companiesRootComponent = CompaniesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			mainContext,
			ioContext,
			companiesRepository,
			(findNavController()::popBackStack),
			mainExecutor,
			(::navigateToFlow),
			startConfiguration,
			(::popBackStack),
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				CompaniesRootContent(companiesRootComponent)
			}
		}
	}

	private fun navigateToFlow(navigationFlow: NavigationFlow) {
		(requireActivity() as ToFlowNavigatable).navigateToFlow(navigationFlow)
	}

	private fun popBackStack() {
		(requireActivity() as ToFlowNavigatable).popBackStack()
	}

	//endregion

}
