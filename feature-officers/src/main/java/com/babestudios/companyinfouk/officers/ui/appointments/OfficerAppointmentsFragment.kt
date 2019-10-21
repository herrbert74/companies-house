package com.babestudios.companyinfouk.officers.ui.appointments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.ScreenState
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsAdapter
import com.babestudios.companyinfouk.officers.ui.appointments.list.OfficerAppointmentsTypeFactory
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
		//TODO
		//logScreenView(this.localClassName)
		val activity = (activity as AppCompatActivity)
		val toolbar = pabOfficerAppointments.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		//TODO
		// pabOfficerAppointments.setNavigationOnClickListener { onBackPressed() }
		activity.supportActionBar?.setTitle(R.string.officer_appointments_title)
		/*when {
			viewModel.state.value.appointmentItems != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<OfficerAppointmentsState>("STATE")?.let {
					with(viewModel.state.value) {
						appointmentItems = it.appointmentItems
						officerId = it.officerId
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.officerId = intent.getStringExtra(OFFICER_ID)
				initPresenter(viewModel)
			}
		}*/

		createRecyclerView()
		viewModel.fetchAppointments()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	//TODO
	/*override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}*/

	/*private fun initPresenter(viewModel: OfficerAppointmentsViewModel) {
		if (!::officerAppointmentsPresenter.isInitialized) {
			comp = DaggerOfficerAppointmentsComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			officerAppointmentsPresenter = comp.officerAppointmentsPresenter()
			officerAppointmentsPresenter.setViewModel(viewModel, requestScope())
		}
	}*/

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

	/*override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}*/

	//endregion

	//region render

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		//TODO
		/*officerAppointmentsAdapter?.getViewClickedObservable()
				?.take(1)
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractOfficerAppointmentsVisitable> ->
					viewModel.state.value.appointmentItems?.let { appointmentItems ->
						val company = (appointmentItems[(view as OfficerAppointmentsViewHolder).adapterPosition] as OfficerAppointmentsVisitable).appointment.appointedTo
						company?.let { appointedTo ->
							startActivityWithRightSlide(this.createCompanyIntent(appointedTo.companyNumber!!, appointedTo.companyName!!))
						}
					}
				}
				?.let { eventDisposables.add(it) }*/
	}

	//endregion

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officerAppointmentsScreenState) {
				is ScreenState.Loading -> msvOfficerAppointments.viewState = VIEW_STATE_LOADING
				is ScreenState.Error -> {
					msvOfficerAppointments.viewState = VIEW_STATE_ERROR
					val tvMsvError = msvOfficerAppointments.findViewById<TextView>(R.id.tvMsvError)
					tvMsvError.text = state.officerAppointmentsScreenState.errorType.message
				}
				is ScreenState.Empty -> msvOfficerAppointments.viewState = VIEW_STATE_EMPTY
				is ScreenState.Complete -> {
					withState(viewModel) {
						msvOfficerAppointments.viewState = VIEW_STATE_CONTENT
						lblOfficerAppointmentsHeaderOfficerName.text = state.officerName
						if (rvOfficerAppointments?.adapter == null) {
							officerAppointmentsAdapter = OfficerAppointmentsAdapter(it.appointmentItems, OfficerAppointmentsTypeFactory())
							rvOfficerAppointments?.adapter = officerAppointmentsAdapter
						} else {
							officerAppointmentsAdapter?.updateItems(it.appointmentItems)
						}
						observeActions()
					}
				}
			}
		}
	}
}
