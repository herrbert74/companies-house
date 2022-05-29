package com.babestudios.companyinfouk.charges.ui.charges

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
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.charges.databinding.FragmentChargesBinding
import com.babestudios.companyinfouk.charges.ui.ChargesViewModel
import com.babestudios.companyinfouk.charges.ui.ChargesViewModelFactory
import com.babestudios.companyinfouk.charges.ui.charges.ChargesStore.State
import com.babestudios.companyinfouk.charges.ui.charges.list.ChargesAdapter
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChargesFragment : Fragment(R.layout.fragment_charges), MviView<State, UserIntent> {

	@Inject
	lateinit var chargesViewModelFactory: ChargesViewModelFactory

	private val args: ChargesFragmentArgs by navArgs()

	private var chargesAdapter: ChargesAdapter? = null

	private val viewModel: ChargesViewModel by viewModels {
		ChargesViewModel.provideFactory(
			chargesViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentChargesBinding>()

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.ChargesItemClicked ->
				findNavController().navigateSafe(
					ChargesFragmentDirections.actionToDetails(sideEffect.selectedChargesItem)
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
			is State.Loading ->  binding.msvCharges.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				if (model.t is OfflineException) {
					binding.msvCharges.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvCharges.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvCharges.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = model.t.message
				}
			}
			is State.Show -> {
				binding.msvCharges.viewState = VIEW_STATE_CONTENT
				if (binding.rvCharges.adapter == null) {
					chargesAdapter = ChargesAdapter(model.charges.items, lifecycleScope)
					binding.rvCharges.adapter = chargesAdapter
				} else {
					chargesAdapter?.updateItems(model.charges.items)
				}
				chargesAdapter?.itemClicks?.onEach {
					dispatch(UserIntent.ChargesItemClicked(it))
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
		(activity as AppCompatActivity).setSupportActionBar(binding.pabCharges.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabCharges.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.charges)
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
		binding.rvCharges.layoutManager = linearLayoutManager
		binding.rvCharges.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvCharges.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				dispatch(UserIntent.LoadMoreCharges(page))
			}
		})
	}

	//endregion
}

sealed class UserIntent {
	data class ChargesItemClicked(val selectedChargesItem: ChargesItem) : UserIntent()
	data class LoadMoreCharges(val page: Int) : UserIntent()
}

sealed class SideEffect {
	data class ChargesItemClicked(val selectedChargesItem: ChargesItem) : SideEffect()
}
