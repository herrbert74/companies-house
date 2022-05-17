package com.babestudios.companyinfouk.officers.ui.officers

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
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
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficersBinding
import com.babestudios.companyinfouk.officers.ui.OfficersActivity
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.OfficersViewModelFactory
import com.babestudios.companyinfouk.officers.ui.officers.OfficersStore.State
import com.babestudios.companyinfouk.officers.ui.officers.list.OfficersAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OfficersFragment : Fragment(R.layout.fragment_officers), MviView<State, UserIntent> {

	@Inject
	lateinit var officersViewModelFactory: OfficersViewModelFactory

	private var officersAdapter: OfficersAdapter? = null

	private val viewModel: OfficersViewModel by activityViewModels {
		OfficersViewModel.provideFactory(
			officersViewModelFactory,
			requireActivity().intent.getStringExtra(COMPANY_NUMBER).orEmpty()
		)
	}

	private val binding by viewBinding<FragmentOfficersBinding>()

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.OfficerItemClicked ->
				(activity as OfficersActivity).officersNavigator.officersToOfficerDetails(sideEffect.selectedOfficer)
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
			is State.Loading -> binding.msvOfficers.viewState = VIEW_STATE_LOADING
			is State.Error -> {
				if (model.t is OfflineException) {
					binding.msvOfficers.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvOfficers.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvOfficers.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = model.t.message
				}
			}
			is State.Show -> {
				binding.msvOfficers.viewState = VIEW_STATE_CONTENT
				if (binding.rvOfficers.adapter == null) {
					officersAdapter = OfficersAdapter(model.officersResponse.items, lifecycleScope)
					binding.rvOfficers.adapter = officersAdapter
				} else {
					officersAdapter?.updateItems(model.officersResponse.items)
				}
				officersAdapter?.itemClicks?.onEach {
					dispatch(UserIntent.OfficerItemClicked(it))
				}?.launchIn(lifecycleScope)
			}
		}
	}

	//endregion

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeUI()
	}

	private fun initializeUI() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabOfficers.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficers.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.setTitle(R.string.officers)
		createRecyclerView()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
		binding.rvOfficers.layoutManager = linearLayoutManager
		binding.rvOfficers.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvOfficers.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				dispatch(UserIntent.LoadMoreOfficers(page))
			}
		})
	}

	//endregion

}

sealed class UserIntent {
	data class OfficerItemClicked(val selectedOfficer: Officer) : UserIntent()
	data class LoadMoreOfficers(val page: Int) : UserIntent()
}

sealed class SideEffect {
	data class OfficerItemClicked(val selectedOfficer: Officer) : SideEffect()
}
