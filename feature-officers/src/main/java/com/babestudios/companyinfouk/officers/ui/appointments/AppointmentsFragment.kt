package com.babestudios.companyinfouk.officers.ui.appointments

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_EMPTY
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.domain.model.officers.Appointment
import com.babestudios.companyinfouk.navigation.DeepLinkDestination
import com.babestudios.companyinfouk.navigation.deepLinkNavigateTo
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficerAppointmentsBinding
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.OfficersViewModelFactory
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsFragment.UserIntent
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State.Error
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsStore.State.Loading
import com.babestudios.companyinfouk.officers.ui.appointments.list.AppointmentsAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AppointmentsFragment : Fragment(R.layout.fragment_officer_appointments), MviView<State, UserIntent> {

	@Inject
	lateinit var officersViewModelFactory: OfficersViewModelFactory

	private var appointmentsAdapter: AppointmentsAdapter? = null

	private val args: AppointmentsFragmentArgs by navArgs()

	private val viewModel: OfficersViewModel by viewModels {
		OfficersViewModel.provideFactory(
			officersViewModelFactory,
			args.selectedCompanyId
		)
	}

	private val binding by viewBinding<FragmentOfficerAppointmentsBinding>()

	private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			findNavController().popBackStack()
		}
	}

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

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
			is Loading -> binding.msvOfficerAppointments.viewState = VIEW_STATE_LOADING
			is Error -> {
				binding.msvOfficerAppointments.viewState = VIEW_STATE_ERROR
				val tvMsvError = binding.msvOfficerAppointments.findViewById<TextView>(R.id.tvMsvError)
				tvMsvError.text = model.t.message
			}
			is State.Show -> {
				if (model.appointments.isEmpty()) {
					binding.msvOfficerAppointments.viewState = VIEW_STATE_EMPTY
				} else {
					binding.msvOfficerAppointments.viewState = VIEW_STATE_CONTENT
					binding.rowOfficerAppointmentsHeader
						.lblOfficerAppointmentsHeaderOfficerName.text = model.selectedOfficer.name
					if (binding.rvOfficerAppointments.adapter == null) {
						appointmentsAdapter = AppointmentsAdapter(model.appointments, lifecycleScope)
						binding.rvOfficerAppointments.adapter = appointmentsAdapter
						appointmentsAdapter?.itemClicks?.onEach {
							dispatch(UserIntent.AppointmentClicked(it))
						}?.launchIn(lifecycleScope)
					} else {
						appointmentsAdapter?.updateItems(model.appointments)
					}
				}
			}
		}
	}

	//endregion

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle(), args.selectedOfficer)
		initializeUI()
	}

	private fun initializeUI() {
		val activity = (activity as AppCompatActivity)
		val toolbar = binding.pabOfficerAppointments.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficerAppointments.setNavigationOnClickListener {
			findNavController().popBackStack()
		}
		activity.supportActionBar?.setTitle(R.string.officer_appointments_title)
		createRecyclerView()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
		binding.rvOfficerAppointments.layoutManager = linearLayoutManager
		binding.rvOfficerAppointments.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvOfficerAppointments.addOnScrollListener(

			object : EndlessRecyclerViewScrollListener(linearLayoutManager) {

				override fun onLoadMore(page: Int, totalItemsCount: Int) {
					dispatch(UserIntent.LoadMoreAppointments(page))
				}

			}

		)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
	}

	//endregion

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.AppointmentClicked ->
				findNavController().deepLinkNavigateTo(
					DeepLinkDestination.Company(sideEffect.companyNumber, sideEffect.companyName)
				)
		}
	}

	sealed class UserIntent {
		data class AppointmentClicked(val selectedAppointment: Appointment) : UserIntent()
		data class LoadMoreAppointments(val page: Int) : UserIntent()
	}

	sealed class SideEffect {
		data class AppointmentClicked(val companyNumber: String, val companyName: String) : SideEffect()
	}

}
