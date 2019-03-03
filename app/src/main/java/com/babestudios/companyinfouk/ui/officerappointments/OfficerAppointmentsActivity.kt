package com.babestudios.companyinfouk.ui.officerappointments

import android.content.Context
import android.content.Intent
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_officer_appointments2.*
import com.babestudios.companyinfouk.R
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.mvp.list.BaseViewHolder
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.CompletableSource
import androidx.lifecycle.ViewModelProviders
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import androidx.recyclerview.widget.LinearLayoutManager
import com.babestudios.base.view.DividerItemDecoration
import com.babestudios.base.view.EndlessRecyclerViewScrollListener
import com.babestudios.companyinfouk.ui.company.createCompanyIntent
import com.babestudios.companyinfouk.ui.officerappointments.list.*

import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.row_officer_appointments_header.*

private const val OFFICER_ID = "com.babestudios.companyinfouk.ui.officer_id"

class OfficerAppointmentsActivity : RxAppCompatActivity(), ScopeProvider {


	private var officerAppointmentsAdapter: OfficerAppointmentsAdapter? = null

	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(OfficerAppointmentsViewModel::class.java) }

	private lateinit var officerAppointmentsPresenter: OfficerAppointmentsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officer_appointments2)
		setSupportActionBar(pabOfficerAppointments.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabOfficerAppointments.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.officer_appointments_title)
		when {
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
		}

		createRecyclerView()
		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: OfficerAppointmentsViewModel) {
		if (!::officerAppointmentsPresenter.isInitialized) {
			officerAppointmentsPresenter = Injector.get().officerAppointmentsPresenter()
			officerAppointmentsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	private fun createRecyclerView() {
		val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
		rvOfficerAppointments?.layoutManager = linearLayoutManager
		rvOfficerAppointments.addItemDecoration(DividerItemDecoration(this))
		rvOfficerAppointments.addOnScrollListener(object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
			override fun onLoadMore(page: Int, totalItemsCount: Int) {
				officerAppointmentsPresenter.loadMoreAppointments(page)
			}
		})
	}


	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: OfficerAppointmentsState) {
		when {
			state.isLoading -> msvOfficerAppointments.viewState = VIEW_STATE_LOADING
			state.errorType != ErrorType.NONE -> msvOfficerAppointments.viewState = VIEW_STATE_ERROR
			state.appointmentItems == null -> msvOfficerAppointments.viewState = VIEW_STATE_EMPTY
			else -> {
				state.appointmentItems?.let {
					msvOfficerAppointments.viewState = VIEW_STATE_CONTENT
					textViewOfficerName.text = state.officerName
					if (rvOfficerAppointments?.adapter == null) {
						officerAppointmentsAdapter = OfficerAppointmentsAdapter(it, OfficerAppointmentsTypeFactory())
						rvOfficerAppointments?.adapter = officerAppointmentsAdapter
					} else {
						officerAppointmentsAdapter?.updateItems(it)
					}
					observeActions()
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
				?.`as`(AutoDispose.autoDisposable(this))
				?.subscribe { view: BaseViewHolder<AbstractOfficerAppointmentsVisitable> ->
					viewModel.state.value.appointmentItems?.let { appointmentItems ->
						val company = (appointmentItems[(view as OfficerAppointmentsViewHolder).adapterPosition] as OfficerAppointmentsVisitable).appointment.appointedTo
						company?.let { appointedTo ->
							startActivityWithRightSlide(this.createCompanyIntent(appointedTo.companyNumber!!, appointedTo.companyName!!))
						}
					}
				}
				?.let { eventDisposables.add(it) }
	}

	//endregion
}

fun Context.createOfficerAppointmentsIntent(officerId: String): Intent {
	return Intent(this, OfficerAppointmentsActivity::class.java)
			.putExtra(OFFICER_ID, officerId)
}
