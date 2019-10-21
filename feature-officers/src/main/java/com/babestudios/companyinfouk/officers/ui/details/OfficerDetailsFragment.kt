package com.babestudios.companyinfouk.officers.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.mvrx.ScreenState
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.officers.ui.OfficersViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_officer_details.*

class OfficerDetailsFragment : BaseMvRxFragment() {

	private val viewModel by activityViewModel(OfficersViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_officer_details, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//TODO
		//logScreenView(this.localClassName)
		val activity = (activity as AppCompatActivity)
		val toolbar = pabOfficerDetails.getToolbar()
		activity.setSupportActionBar(toolbar)
		activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
		//TODO
		// pabOfficerDetails.setNavigationOnClickListener { onBackPressed() }
		activity.supportActionBar?.setTitle(R.string.officer_details)
		/*when {
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
		}*/
		invalidate()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	/*override fun onSaveInstanceState(outState: Bundle) {
		outState.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}*/

/*private fun initPresenter(viewModel: OfficerDetailsViewModel) {
	if (!::officerDetailsPresenter.isInitialized) {
		comp = DaggerOfficerDetailsComponent
				.builder()
				.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
				.build()
		officerDetailsPresenter = comp.officerDetailsPresenter()
		officerDetailsPresenter.setViewModel(viewModel, requestScope())
	}
}*/

	/*override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}*/

//endregion

//region events

	//TODO
	@Suppress("CheckResult")
	private fun observeActions() {
		eventDisposables.clear()
		val extras = FragmentNavigatorExtras(
				btnOfficerDetailsAppointments to "pabOfficerAppointments")
		RxView.clicks(btnOfficerDetailsAppointments)
		//		.`as`(AutoDispose.autoDisposable(this))
				.subscribe { viewModel.officerAppointmentsClicked(extras) }
	}

	//endregion

	//region render

	override fun invalidate() {
		withState(viewModel) { state ->
			when (state.officerDetailsScreenState) {
				is ScreenState.Complete -> {
					showOfficerDetails()
				}
			}
		}
	}

	private fun showOfficerDetails() {
		withState(viewModel) { state ->
			val officerItem = state.officerItem
			lblOfficerDetailsName?.text = officerItem?.name
			lblOfficerDetailsAppointedOn?.text = officerItem?.appointedOn
			lblOfficerDetailsNationality?.text = officerItem?.nationality
			lblOfficerDetailsOccupation?.text = officerItem?.occupation
			officerItem?.dateOfBirth?.let {
				lblOfficerDetailsDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
			} ?: run {
				lblOfficerDetailsDateOfBirth?.setText(R.string.officer_details_unknown)
			}
			lblOfficerDetailsCountryOfResidence?.text = officerItem?.countryOfResidence
			lblOfficerDetailsAddressLine1?.text = officerItem?.address?.addressLine1
			lblOfficerDetailsLocality?.text = officerItem?.address?.locality
			lblOfficerDetailsPostalCode?.text = officerItem?.address?.postalCode
			officerItem?.address?.region?.let {
				lblOfficerDetailsRegion?.visibility = View.VISIBLE
				lblOfficerDetailsRegion?.text = it
			} ?: run {
				lblOfficerDetailsRegion?.visibility = View.GONE
			}
			officerItem?.address?.country?.let {
				lblOfficerDetailsCountry?.text = it
				lblOfficerDetailsCountry?.visibility = View.VISIBLE
			} ?: run {
				lblOfficerDetailsCountry?.visibility = View.GONE
			}
		}
	}

//endregion

}
