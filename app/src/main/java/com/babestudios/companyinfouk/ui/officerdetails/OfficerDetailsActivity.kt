package com.babestudios.companyinfouk.ui.officerdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

import kotlinx.android.synthetic.main.activity_officer_details.*
import com.babestudios.companyinfouk.R
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.CompletableSource
import androidx.lifecycle.ViewModelProviders
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.officerappointments.createOfficerAppointmentsIntent
import com.jakewharton.rxbinding2.view.RxView

import io.reactivex.disposables.CompositeDisposable

private const val OFFICER_ITEM = "com.babestudios.companyinfouk.ui.officer_item"

class OfficerDetailsActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(OfficerDetailsViewModel::class.java) }

	private lateinit var officerDetailsPresenter: OfficerDetailsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_officer_details)
		setSupportActionBar(pabOfficerDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabOfficerDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.officer_details)
		when {
			viewModel.state.value.officerItem != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<OfficerDetailsState>("STATE")?.let {
					with(viewModel.state.value) {

						officerItem = it.officerItem
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.officerItem = intent.getParcelableExtra(OFFICER_ITEM)
				initPresenter(viewModel)
			}
		}

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

	private fun initPresenter(viewModel: OfficerDetailsViewModel) {
		if (!::officerDetailsPresenter.isInitialized) {
			officerDetailsPresenter = Injector.get().officerDetailsPresenter()
			officerDetailsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: OfficerDetailsState) {
		when {
			state.contentChange == ContentChange.OFFICER_ITEM_RECEIVED -> {
				state.contentChange = ContentChange.NONE
				showOfficerDetails()
			}
		}
	}

	private fun showOfficerDetails() {
		val officerItem = viewModel.state.value.officerItem
		textViewName?.text = officerItem?.name
		textViewAppointedOn?.text = officerItem?.appointedOn
		textViewNationality?.text = officerItem?.nationality
		textViewOccupation?.text = officerItem?.occupation
		officerItem?.dateOfBirth?.let {
			textViewDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
		} ?: run {
			textViewDateOfBirth?.setText(R.string.officer_details_unknown)
		}
		textViewCountryOfResidence?.text = officerItem?.countryOfResidence
		textViewAddressLine1?.text = officerItem?.address?.addressLine1
		textViewLocality?.text = officerItem?.address?.locality
		textViewPostalCode?.text = officerItem?.address?.postalCode
		officerItem?.address?.region?.let {
			textViewRegion?.visibility = View.VISIBLE
			textViewRegion?.text = it
		} ?: run {
			textViewRegion?.visibility = View.GONE
		}
		officerItem?.address?.country?.let {
			textViewCountry?.text = it
			textViewCountry?.visibility = View.VISIBLE
		} ?: run {
			textViewCountry?.visibility = View.GONE
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		RxView.clicks(buttonAppointments)
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { startActivityWithRightSlide(createOfficerAppointmentsIntent(viewModel.state.value.officerId)) }

	}

	//endregion
}

fun Context.createOfficerDetailsIntent(officerItem: OfficerItem): Intent {
	return Intent(this, OfficerDetailsActivity::class.java)
			.putExtra(OFFICER_ITEM, officerItem)
}
