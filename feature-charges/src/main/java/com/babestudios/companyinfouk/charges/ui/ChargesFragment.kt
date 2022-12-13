package com.babestudios.companyinfouk.charges.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.decompose.DefaultComponentContext
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.ui.charges.ChargesExecutor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class ChargesFragment : Fragment(R.layout.fragment_charges) {

	@Inject
	lateinit var chargesExecutor: ChargesExecutor

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	private val args: ChargesFragmentArgs by navArgs()

	private lateinit var chargesRootComponent: ChargesRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		chargesRootComponent = ChargesRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			chargesExecutor,
			mainContext,
			args.selectedCompanyId,
			(findNavController()::popBackStack),
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				ChargesRootContent(chargesRootComponent)
			}
		}
	}

	//endregion

}
