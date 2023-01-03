package com.babestudios.companyinfouk.insolvencies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesExecutor
import com.babestudios.companyinfouk.navigation.DeepLinkDestination
import com.babestudios.companyinfouk.navigation.deepLinkNavigateTo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class InsolvenciesFragment : Fragment(R.layout.fragment_insolvencies) {

	@Inject
	lateinit var insolvenciesExecutor: InsolvenciesExecutor

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	private val args: InsolvenciesFragmentArgs by navArgs()

	private lateinit var insolvenciesRootComponent: InsolvenciesRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		insolvenciesRootComponent = InsolvenciesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			insolvenciesExecutor,
			mainContext,
			args.selectedCompanyId,
			(findNavController()::popBackStack),
			(::navigateToMap),
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				InsolvenciesRootContent(insolvenciesRootComponent)
			}
		}
	}

	private fun navigateToMap(name: String, address: Address) {
		findNavController().deepLinkNavigateTo(
			DeepLinkDestination.Map(
				name,
				address.getAddressString(),
			)
		)
	}

	//endregion

}
