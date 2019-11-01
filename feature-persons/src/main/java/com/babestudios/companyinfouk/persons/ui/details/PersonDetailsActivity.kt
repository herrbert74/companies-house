package com.babestudios.companyinfouk.persons.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.babestudios.base.ext.biLet
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.model.persons.Person
import com.babestudios.companyinfouk.ext.logScreenView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_person_details.*

private const val PERSON_ITEM = "com.babestudios.companyinfouk.ui.person_item"

class PersonDetailsActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(PersonDetailsViewModel::class.java) }

	private lateinit var personDetailsPresenter: PersonDetailsPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_person_details)
		logScreenView(this.localClassName)
		setSupportActionBar(pabPersonDetails.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabPersonDetails.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.setTitle(R.string.person_details)
		when {
			viewModel.state.value?.person != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				(savedInstanceState.getParcelable<PersonDetailsState>("STATE") to viewModel.state.value)
						.biLet { savedState, state ->
							state.person = savedState.person
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value?.person = intent.getParcelableExtra(PERSON_ITEM)
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

	private fun initPresenter(viewModel: PersonDetailsViewModel) {
		if (!::personDetailsPresenter.isInitialized) {
			comp = DaggerPersonDetailsComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			personDetailsPresenter = comp.personDetailsPresenter()
			personDetailsPresenter.setViewModel(viewModel, requestScope())
		}
	}

	override fun onBackPressed() {
		super.finish()
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: PersonDetailsState) {
		state.person?.let {
			showPersonDetails(it)
		}
	}

	private fun showPersonDetails(person: Person) {
		lblPersonDetailsName?.text = person.name
		lblPersonDetailsNotifiedOn?.text = person.notifiedOn
		if (person.nationality == null) {
			lblPersonDetailsNationality?.visibility = View.GONE
			cpnPersonDetailsNationality?.visibility = View.GONE
		} else {
			lblPersonDetailsNationality?.visibility = View.VISIBLE
			lblPersonDetailsNationality?.text = person.nationality
		}
		if (person.countryOfResidence == null) {
			lblPersonDetailsCountryOfResidence?.visibility = View.GONE
			cpnPersonDetailsCountryOfResidence?.visibility = View.GONE
		} else {
			lblPersonDetailsCountryOfResidence?.visibility = View.VISIBLE
			lblPersonDetailsCountryOfResidence?.text = person.countryOfResidence
		}
		person.dateOfBirth?.let {
			lblPersonDetailsDateOfBirth?.visibility = View.VISIBLE
			lblPersonDetailsDateOfBirth?.text = "${it.month.toString()} / ${it.year.toString()}"
		} ?: run {
			lblPersonDetailsDateOfBirth?.visibility = View.GONE
			cpnPersonDetailsDateOfBirth?.visibility = View.GONE
		}
		val naturesOfControl = StringBuilder()
		for (i in person.naturesOfControl.indices) {
			naturesOfControl.append(person.naturesOfControl[i])
			if (i < person.naturesOfControl.size - 1) {
				naturesOfControl.append("; ")
			}
		}
		lblPersonDetailsNaturesOfControl?.text = naturesOfControl.toString()

		lblPersonDetailsAddress?.text = person.address?.addressLine1
		lblPersonDetailsLocality?.text = person.address?.locality
		lblPersonDetailsPostalCode?.text = person.address?.postalCode
		person.identification?.placeRegistered?.let {
			lblPersonDetailsPlaceRegistered?.text = it
			lblPersonDetailsPlaceRegistered?.visibility = View.VISIBLE
		} ?: run {
			lblPersonDetailsPlaceRegistered?.visibility = View.GONE
			cpnPersonDetailsPlaceRegistered?.visibility = View.GONE
		}
		person.identification?.registrationNumber?.let {
			lblPersonDetailsRegistrationNumber?.text = it
			lblPersonDetailsRegistrationNumber?.visibility = View.VISIBLE
		} ?: run {
			cpnPersonDetailsRegistrationNumber?.visibility = View.GONE
			lblPersonDetailsRegistrationNumber?.visibility = View.GONE
		}
		if (person.address?.region == null) {
			lblPersonDetailsRegion?.visibility = View.GONE
		} else {
			lblPersonDetailsRegion?.visibility = View.VISIBLE
			lblPersonDetailsRegion?.text = person.address?.region
		}
		person.address?.country?.let {
			lblPersonDetailsCountry?.text = it
			lblPersonDetailsCountry?.visibility = View.VISIBLE
		} ?: run {
			lblPersonDetailsCountry?.visibility = View.GONE
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
	}

	//endregion
}

fun Context.createPersonDetailsIntent(person: Person): Intent {
	return Intent(this, PersonDetailsActivity::class.java)
			.putExtra(PERSON_ITEM, person)
}
