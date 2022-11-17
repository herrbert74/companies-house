package com.babestudios.companyinfouk.persons.ui.persons

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
import com.babestudios.companyinfouk.navigation.DeepLinkDestination
import com.babestudios.companyinfouk.navigation.deepLinkNavigateTo
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.persons.ui.PersonsRootComponent
import com.babestudios.companyinfouk.persons.ui.PersonsRootContent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class PersonsFragment : Fragment(R.layout.fragment_persons) {

	@Inject
	lateinit var personsExecutor: PersonsExecutor

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	private val args: PersonsFragmentArgs by navArgs()

	private lateinit var personsRootComponent: PersonsRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		personsRootComponent = PersonsRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			personsExecutor,
			mainContext,
			args.selectedCompanyId,
			(findNavController()::popBackStack),
			(::navigateToMap)
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				PersonsRootContent(personsRootComponent)
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
