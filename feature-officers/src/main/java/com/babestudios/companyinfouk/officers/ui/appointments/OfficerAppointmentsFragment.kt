package com.babestudios.companyinfouk.officers.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.*
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.appointments.list.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_officer_appointments.*
import kotlinx.android.synthetic.main.row_officer_appointments_header.*

class OfficerAppointmentsFragment : BaseMvRxFragment() {


	private var officerAppointmentsAdapter: OfficerAppointmentsAdapter? = null

	private val viewModel by activityViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_officer_appointments, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		val activity = (activity as AppCompatActivity)
		val toolbar = pabOfficerAppointments.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabOfficerAppointments.setNavigationOnClickListener { viewModel.officersNavigator.popBackStack() }
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
		rvOfficerAppointments?.layoutManager = linearLayoutManager
		rvOfficerAppointments.addItemDecoration(DividerItemDecoration(requireContext()))
		rvOfficerAppointments.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				viewModel.loadMoreAppointments(page)
			}
		})
	}

	//endregion

	//region render

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		//TODO
		officerAppointmentsAdapter?.getViewClickedObservable()
				?.take(1)
				?.subscribe { view: BaseViewHolder<AbstractOfficerAppointmentsVisitable> ->
					withState(viewModel) {state->
						state.appointmentItems.let { appointmentItems ->
							val company = (appointmentItems[(view as OfficerAppointmentsViewHolder).adapterPosition] as OfficerAppointmentsVisitable).appointment.appointedTo
							company?.let { appointedTo ->
								/*(activity as AppCompatActivity).startActivityWithRightSlide(
										activity.createCompanyIntent(appointedTo.companyNumber!!, appointedTo.companyName!!))*/
								viewModel.officersNavigator
										.officersAppointmentsToCompanyActivity(
												appointedTo.companyNumber!!,
												appointedTo.companyName!!
										)
							}
						}
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officerAppointmentsRequest) {
				is Loading -> msvOfficerAppointments.viewState = VIEW_STATE_LOADING
				is Fail -> {
					msvOfficerAppointments.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvOfficerAppointments.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officerAppointmentsRequest.error.message
				}
				is Success -> {
					withState(viewModel) {
						if (it.appointmentItems.isEmpty()) {
							msvOfficerAppointments.viewState = VIEW_STATE_EMPTY
						} else {
							msvOfficerAppointments.viewState = VIEW_STATE_CONTENT
							lblOfficerAppointmentsHeaderOfficerName.text = state.officerName
							if (rvOfficerAppointments?.adapter == null) {
								officerAppointmentsAdapter = OfficerAppointmentsAdapter(it.appointmentItems, OfficerAppointmentsTypeFactory())
								rvOfficerAppointments?.adapter = officerAppointmentsAdapter
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
}
