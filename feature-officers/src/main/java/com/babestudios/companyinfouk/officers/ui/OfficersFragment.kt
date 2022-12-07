package com.babestudios.companyinfouk.officers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.navigation.DeepLinkDestination
import com.babestudios.companyinfouk.navigation.deepLinkNavigateTo
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsExecutor
import com.babestudios.companyinfouk.officers2.R
import com.babestudios.companyinfouk.officers.ui.officers.OfficersExecutor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class OfficersFragment : Fragment(R.layout.fragment_officers) {

	@Inject
	lateinit var officersExecutor: OfficersExecutor

	@Inject
	lateinit var appointmentsExecutor: AppointmentsExecutor

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	private val args: OfficersFragmentArgs by navArgs()

	private lateinit var officersRootComponent: OfficersRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		officersRootComponent = OfficersRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			officersExecutor,
			companiesRepository,
			mainContext,
			ioContext,
			args.selectedCompanyId,
			(findNavController()::popBackStack),
			(::navigateToMap),
			(::navigateToCompany)
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				OfficersRootContent(officersRootComponent)
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

	private fun navigateToCompany(companyNumber: String, companyName: String) {
		findNavController().deepLinkNavigateTo(
			DeepLinkDestination.Company(
				companyNumber,
				companyName,
			)
		)
	}

	//endregion

}
