package com.babestudios.companyinfouk.companies.ui.company

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.ViewModelProviders
import com.babestudios.base.ext.biLet
import com.babestudios.base.ext.convertToTimestamp
import com.babestudios.base.ext.formatShortDateFromTimeStampMillis
import com.babestudios.base.ext.parseMySqlDate
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.charges.ui.createChargesIntent
import com.babestudios.companyinfouk.core.injection.CoreInjectHelper
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.ext.logScreenView
import com.babestudios.companyinfouk.common.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.filings.ui.createFilingsIntent
import com.babestudios.companyinfouk.insolvencies.ui.createInsolvenciesIntent
import com.babestudios.companyinfouk.officers.ui.createOfficersIntent
import com.babestudios.companyinfouk.persons.ui.createPersonsIntent
import com.babestudios.companyinfouk.ui.map.MapActivity
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_company.*
import kotlinx.android.synthetic.main.multi_state_view_error.view.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.company_number"
private const val COMPANY_NAME = "com.babestudios.companyinfouk.ui.company_name"

class CompanyActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(CompanyViewModel::class.java) }

	private lateinit var companyPresenter: CompanyPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private lateinit var comp: CompanyComponent

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_company)
		logScreenView(this.localClassName)
		setSupportActionBar(pabCompany.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabCompany.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.title = intent.getStringExtra(COMPANY_NAME)
		when {
			viewModel.state.value?.company != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				(savedInstanceState.getParcelable<CompanyState>("STATE") to viewModel.state.value)
						.biLet { savedState, state ->
							state.company = savedState.company
							state.companyName = savedState.companyName
							state.companyNumber = savedState.companyNumber
							state.addressString = savedState.addressString
							state.isFavorite = savedState.isFavorite
							state.natureOfBusinessString = savedState.natureOfBusinessString
						}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value?.companyNumber = intent.getStringExtra(COMPANY_NUMBER) ?: ""
				viewModel.state.value?.companyName = intent.getStringExtra(COMPANY_NAME) ?: ""
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

	private fun initPresenter(viewModel: CompanyViewModel) {
		val maybePresenter = lastCustomNonConfigurationInstance as CompanyPresenterContract?

		if (maybePresenter != null) {
			companyPresenter = maybePresenter
		}

		if (!::companyPresenter.isInitialized) {
			comp = DaggerCompanyComponent
					.builder()
					.coreComponent(CoreInjectHelper.provideCoreComponent(applicationContext))
					.build()
			companyPresenter = comp.companyPresenter()
			companyPresenter.setViewModel(viewModel, requestScope())
		}
	}

	//endregion

	//region render

	private fun observeState() {
		viewModel.state
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { render(it) }
	}

	private fun render(state: CompanyState) {
		when {
			state.isLoading -> {
				msvCompany.viewState = VIEW_STATE_LOADING
				hideFabToShowFavoriteState(state.isFavorite)
			}
			state.errorType != ErrorType.NONE -> {
				msvCompany.viewState = VIEW_STATE_ERROR
				fabCompanyFavorite.animate().cancel()
				fabCompanyFavorite.hide()
				pabCompany.setExpanded(false)
				msvCompany.tvMsvError.text = state.errorMessage
			}
			state.contentChange == ContentChange.HIDE_FAB -> {
				setResult(Activity.RESULT_OK)
				hideFabToShowFavoriteState(state.isFavorite)
			}
			else -> {
				state.company?.let {
					msvCompany.viewState = VIEW_STATE_CONTENT
					showCompany(it, state.natureOfBusinessString)
				}
			}
		}
	}

	private fun showCompany(company: Company, natureOfBusinessString: String) {
		tvCompanyNumber?.text = company.companyNumber
		lblCompanyNatureOfBusiness.text = natureOfBusinessString
		tvCompanyIncorporated?.text = String.format(resources.getString(R.string.incorporated_on), company.dateOfCreation)
		lblCompanyAddressLine1?.text = company.registeredOfficeAddress?.addressLine1
		if (company.registeredOfficeAddress?.addressLine2 != null) {
			lblCompanyAddressLine2?.visibility = View.VISIBLE
			lblCompanyAddressLine2?.text = company.registeredOfficeAddress?.addressLine2
		}
		lblCompanyAddressPostalCode?.text = company.registeredOfficeAddress?.postalCode
		lblCompanyAddressLocality?.text = company.registeredOfficeAddress?.locality
		var formattedDate: String
		company.accounts?.lastAccounts?.madeUpTo?.let {
			val madeUpToDate = it.parseMySqlDate()
			madeUpToDate?.let { date ->
				formattedDate = (1000 * date.convertToTimestamp()).formatShortDateFromTimeStampMillis()
				lblCompanyAccounts?.text = String.format(resources.getString(R.string.company_accounts_formatted_text), company.accounts?.lastAccounts?.type,
						formattedDate)
			}

		} ?: run {
			lblCompanyAccounts?.text = resources.getString(R.string.company_accounts_not_found)
		}
		company.annualReturn?.lastMadeUpTo?.let {
			val lastMadeUpToDate = it.parseMySqlDate()
			lastMadeUpToDate?.let { date ->
				formattedDate = (1000 * date.convertToTimestamp()).formatShortDateFromTimeStampMillis()
				lblCompanyAnnualReturns?.text = String.format(resources.getString(R.string.company_annual_returns_formatted_text), formattedDate)
			}
		} ?: run {
			lblCompanyAnnualReturns?.text = resources.getString(R.string.company_annual_returns_not_found)
		}

		if (!company.hasCharges) {
			llCompanyCharges.visibility = View.GONE
		}
		if (!company.hasInsolvencyHistory) {
			llCompanyInsolvency.visibility = View.GONE
		}
	}

	private fun showFab(favorite: Boolean) {
		fabCompanyFavorite?.also {
			if (favorite) {
				it.setImageResource(R.drawable.ic_favorite_clear)
			} else {
				it.setImageResource(R.drawable.ic_favorite)
			}
			it.scaleX = 0f
			it.scaleY = 0f
			it.alpha = 0f
			it.show()
			it.animate().setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong()).scaleX(1f).scaleY(1f).alpha(1f).interpolator = LinearOutSlowInInterpolator()
		}
	}

	private fun hideFabToShowFavoriteState(favorite: Boolean) {
		fabCompanyFavorite?.also {
			it.animate().cancel()
			it.animate().setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong()).scaleX(0f).scaleY(0f).alpha(0f)
					.setInterpolator(LinearOutSlowInInterpolator()).withEndAction { this.showFab(favorite) }
		}

	}

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out)
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		RxView.clicks(fabCompanyFavorite)
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { companyPresenter.updateFavorites() }
		RxView.clicks(btnShowOnMap)
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe {
					val intent = Intent(this, MapActivity::class.java)
					intent.putExtra("addressString", viewModel.state.value?.addressString)
					intent.putExtra("companyName", viewModel.state.value?.companyName)
					startActivityWithRightSlide(intent)
				}
	}

	fun onFilingHistoryClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		viewModel.state.value?.companyNumber?.let {
			startActivityWithRightSlide(createFilingsIntent(it))
		}
	}

	fun onChargesClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		viewModel.state.value?.companyNumber?.let {
			startActivityWithRightSlide(createChargesIntent(it))
		}
	}

	fun onInsolvencyClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		viewModel.state.value?.companyNumber?.let {
			startActivityWithRightSlide(createInsolvenciesIntent(it))
		}
	}

	fun onOfficersClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		viewModel.state.value?.companyNumber?.let {
			startActivityWithRightSlide(createOfficersIntent(it))
		}
	}

	fun onPersonsClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		viewModel.state.value?.companyNumber?.let {
			startActivityWithRightSlide(createPersonsIntent(it))
		}
	}

	//endregion
}

fun Context.createCompanyIntent(companyNumber: String, companyName: String): Intent {
	return Intent(this, CompanyActivity::class.java)
			.putExtra(COMPANY_NAME, companyName)
			.putExtra(COMPANY_NUMBER, companyNumber)
}
