package com.babestudios.companyinfouk.officers.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.databinding.FragmentOfficerAppointmentsBinding
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.appointments.list.*
import io.reactivex.disposables.CompositeDisposable

class OfficerAppointmentsFragment : BaseMvRxFragment() {


	private var officerAppointmentsAdapter: OfficerAppointmentsAdapter? = null

	private val viewModel by existingViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentOfficerAppointmentsBinding? = null
	private val binding get() = _binding!!

	private val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			viewModel.officersNavigator.popBackStack()
		}
	}

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		_binding = FragmentOfficerAppointmentsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		val activity = (activity as AppCompatActivity)
		val toolbar = binding.pabOfficerAppointments.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabOfficerAppointments.setNavigationOnClickListener { viewModel.officersNavigator.popBackStack() }
		activity.supportActionBar?.setTitle(R.string.officer_appointments_title)
		createRecyclerView()
		viewModel.fetchAppointments()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
		binding.rvOfficerAppointments.layoutManager = linearLayoutManager
		binding.rvOfficerAppointments.addItemDecoration(DividerItemDecoration(requireContext()))
		binding.rvOfficerAppointments.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreAppointments(page)
			}
		})
	}

	override fun onDestroyView() {
		super.onDestroyView()
		callback.remove()
		_binding = null
	}

	//endregion

	//region render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officerAppointmentsRequest) {
				is Loading -> binding.msvOfficerAppointments.viewState = VIEW_STATE_LOADING
				is Fail -> {
					binding.msvOfficerAppointments.viewState = VIEW_STATE_ERROR
					val tvMsvError = binding.msvOfficerAppointments.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officerAppointmentsRequest.error.message
				}
				is Success -> {
					withState(viewModel) {
						if (it.appointmentItems.isEmpty()) {
							binding.msvOfficerAppointments.viewState = VIEW_STATE_EMPTY
						} else {
							binding.msvOfficerAppointments.viewState = VIEW_STATE_CONTENT
							binding.rowOfficerAppointmentsHeader
									.lblOfficerAppointmentsHeaderOfficerName.text = state.officerName
							if (binding.rvOfficerAppointments.adapter == null) {
								officerAppointmentsAdapter = OfficerAppointmentsAdapter(it.appointmentItems, OfficerAppointmentsTypeFactory())
								binding.rvOfficerAppointments.adapter = officerAppointmentsAdapter
							} else {
								officerAppointmentsAdapter?.updateItems(it.appointmentItems)
							}
						}
						observeActions()
					}
				}
			}
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		officerAppointmentsAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<OfficerAppointmentsVisitableBase> ->
					withState(viewModel) { state ->
						state.appointmentItems.let { appointmentItems ->
							val company =
									(appointmentItems[(view as OfficerAppointmentsViewHolder).adapterPosition]
											as OfficerAppointmentsVisitable)
											.appointment
											.appointedTo
							company.let { appointedTo ->
								viewModel.officersNavigator
										.officersAppointmentsToCompanyActivity(
												appointedTo.companyNumber,
												appointedTo.companyName
										)
							}
						}
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}
