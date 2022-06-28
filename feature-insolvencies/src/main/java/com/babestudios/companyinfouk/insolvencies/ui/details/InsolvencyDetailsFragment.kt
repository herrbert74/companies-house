package com.babestudios.companyinfouk.insolvencies.ui.details

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.ext.viewBinding
import com.babestudios.base.view.DividerItemDecorationWithSubHeading
import com.babestudios.base.view.MultiStateView.Companion.VIEW_STATE_CONTENT
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentInsolvencyDetailsBinding
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModelFactory
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsStore.State
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsAdapter
import com.babestudios.companyinfouk.insolvencies.ui.details.list.InsolvencyDetailsTypeFactory
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class InsolvencyDetailsFragment : Fragment(R.layout.fragment_insolvency_details), MviView<State, UserIntent> {

	@Inject
	lateinit var insolvenciesViewModelFactory: InsolvenciesViewModelFactory

	private val args: InsolvencyDetailsFragmentArgs by navArgs()

	private var insolvencyDetailsAdapter: InsolvencyDetailsAdapter? = null

	private val viewModel: InsolvenciesViewModel by viewModels {
		InsolvenciesViewModel.provideFactory(
			insolvenciesViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentInsolvencyDetailsBinding>()

	private val callback: OnBackPressedCallback = (object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	})

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.PractitionerClicked ->
				findNavController().navigateSafe(
					InsolvencyDetailsFragmentDirections.actionToPractitioner(
						sideEffect.companyNumber, sideEffect.selectedPractitioner
					)
				)
		}
	}

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {
		when (model) {
			is State.Show -> {
				binding.msvInsolvencyDetails.viewState = VIEW_STATE_CONTENT
				if (binding.rvInsolvencyDetails.adapter == null) {
					val titlePositions = ArrayList<Int>()
					titlePositions.add(0)
					titlePositions.add(model.datesSize + 1)
					binding.rvInsolvencyDetails.addItemDecoration(
						DividerItemDecorationWithSubHeading(requireContext(), titlePositions)
					)
					insolvencyDetailsAdapter = InsolvencyDetailsAdapter(
						model.insolvencyDetailVisitables,
						InsolvencyDetailsTypeFactory(),
						lifecycleScope
					)
					binding.rvInsolvencyDetails.adapter = insolvencyDetailsAdapter
				} else {
					insolvencyDetailsAdapter?.updateItems(model.insolvencyDetailVisitables)
				}


				insolvencyDetailsAdapter?.itemClicks?.onEach {
					dispatch(UserIntent.PractitionerClicked(it))
				}?.launchIn(lifecycleScope)

			}
			else -> {}
		}
	}

	//endregion

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		viewModel.onViewCreated(this, essentyLifecycle(), args.selectedInsolvency)
		initializeToolBar()
		createRecyclerView()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabInsolvencyDetails.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar

		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabInsolvencyDetails.setNavigationOnClickListener { findNavController().popBackStack() }
		toolBar?.setTitle(R.string.insolvency_details)
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvInsolvencyDetails.layoutManager = linearLayoutManager
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

}

sealed class UserIntent {
	data class PractitionerClicked(val selectedPractitioner: Practitioner) : UserIntent()
}

sealed class SideEffect {
	data class PractitionerClicked(val companyNumber: String, val selectedPractitioner: Practitioner) : SideEffect()
}
