package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.babestudios.base.network.OfflineException
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.insolvencies.databinding.FragmentInsolvencyBinding
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModel
import com.babestudios.companyinfouk.insolvencies.ui.InsolvenciesViewModelFactory
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesStore.State
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.list.InsolvenciesAdapter
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class InsolvenciesFragment : Fragment(R.layout.fragment_insolvency), MviView<State, UserIntent> {

	@Inject
	lateinit var insolvenciesViewModelFactory: InsolvenciesViewModelFactory

	private val args: InsolvenciesFragmentArgs by navArgs()

	private var insolvenciesAdapter: InsolvenciesAdapter? = null

	private val viewModel: InsolvenciesViewModel by viewModels {
		InsolvenciesViewModel.provideFactory(
			insolvenciesViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentInsolvencyBinding>()

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.InsolvencyClicked ->
				findNavController().navigateSafe(
					InsolvenciesFragmentDirections.actionToDetails(
						sideEffect.companyNumber, sideEffect.selectedInsolvency
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
			is State.Loading -> binding.msvInsolvency.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				if (model.t is OfflineException) {
					binding.msvInsolvency.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvInsolvency.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvInsolvency.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = model.t.message
				}
			}
			is State.Show -> {
				binding.msvInsolvency.viewState = VIEW_STATE_CONTENT
				if (binding.rvInsolvency.adapter == null) {
					insolvenciesAdapter = InsolvenciesAdapter(model.insolvencies, lifecycleScope)
					binding.rvInsolvency.adapter = insolvenciesAdapter
				} else {
					insolvenciesAdapter?.updateItems(model.insolvencies)
				}
				insolvenciesAdapter?.itemClicks?.onEach {
					dispatch(UserIntent.InsolvencyClicked(it))
				}?.launchIn(lifecycleScope)
			}
		}
	}

	//endregion

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeToolBar()
		createRecyclerView()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabInsolvency.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabInsolvency.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.insolvency)
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvInsolvency.layoutManager = linearLayoutManager
		binding.rvInsolvency.addItemDecoration(DividerItemDecoration(requireContext()))
	}

	//endregion

}

sealed class UserIntent {
	data class InsolvencyClicked(val selectedInsolvency: InsolvencyCase) : UserIntent()
}

sealed class SideEffect {
	data class InsolvencyClicked(val companyNumber: String, val selectedInsolvency: InsolvencyCase) : SideEffect()
}
