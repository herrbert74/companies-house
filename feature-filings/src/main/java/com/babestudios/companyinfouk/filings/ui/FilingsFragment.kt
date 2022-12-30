package com.babestudios.companyinfouk.filings.ui

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
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryExecutor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@AndroidEntryPoint
class FilingsFragment : Fragment(R.layout.fragment_filings) {
	@Inject
	lateinit var filingHistoryExecutor: FilingHistoryExecutor

	@Inject
	lateinit var companiesRepository: CompaniesRepository

	@Inject
	@MainDispatcher
	lateinit var mainContext: CoroutineDispatcher

	@Inject
	@IoDispatcher
	lateinit var ioContext: CoroutineDispatcher

	private val args: FilingsFragmentArgs by navArgs()

	private lateinit var filingsRootComponent: FilingsRootComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		filingsRootComponent = FilingsRootComponent(
			DefaultComponentContext(lifecycle, savedStateRegistry, viewModelStore, null),
			companiesRepository,
			mainContext,
			ioContext,
			args.selectedCompanyId,
			(findNavController()::popBackStack),
			filingHistoryExecutor,
		)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return ComposeView(requireContext()).apply {
			setContent {
				FilingsRootContent(filingsRootComponent)
			}
		}
	}

	//endregion

}
