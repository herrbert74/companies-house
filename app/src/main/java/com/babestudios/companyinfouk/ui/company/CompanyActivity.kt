package com.babestudios.companyinfouk.ui.company

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.ViewModelProviders
import com.babestudios.base.mvp.ErrorType
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.Injector
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.ext.startActivityWithRightSlide
import com.babestudios.companyinfouk.ui.charges.createChargesIntent
import com.babestudios.companyinfouk.ui.filinghistory.createFilingHistoryIntent
import com.babestudios.companyinfouk.ui.insolvency.createInsolvencyIntent
import com.babestudios.companyinfouk.ui.map.MapActivity
import com.babestudios.companyinfouk.ui.officers.createOfficersIntent
import com.babestudios.companyinfouk.ui.persons.createPersonsIntent
import com.babestudios.companyinfouk.utils.DateUtil
import com.jakewharton.rxbinding2.view.RxView
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_company.*

private const val COMPANY_NUMBER = "com.babestudios.companyinfouk.ui.company_number"
private const val COMPANY_NAME = "com.babestudios.companyinfouk.ui.company_name"

class CompanyActivity : RxAppCompatActivity(), ScopeProvider {


	override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

	private val viewModel by lazy { ViewModelProviders.of(this).get(CompanyViewModel::class.java) }

	private lateinit var companyPresenter: CompanyPresenterContract

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_company)
		setSupportActionBar(pabCompany.getToolbar())
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		pabCompany.setNavigationOnClickListener { onBackPressed() }
		supportActionBar?.title = intent.getStringExtra(COMPANY_NAME)
		when {
			viewModel.state.value.company != null -> {
				initPresenter(viewModel)
			}
			savedInstanceState != null -> {
				savedInstanceState.getParcelable<CompanyState>("STATE")?.let {
					with(viewModel.state.value) {
						company = it.company
						companyName = it.companyName
						companyNumber = it.companyNumber
						addressString = it.addressString
						isFavorite = it.isFavorite
						natureOfBusinessString = it.natureOfBusinessString
					}
				}
				initPresenter(viewModel)
			}
			else -> {
				viewModel.state.value.companyNumber = intent.getStringExtra(COMPANY_NUMBER)
				viewModel.state.value.companyName = intent.getStringExtra(COMPANY_NAME)
				initPresenter(viewModel)
			}
		}

		observeState()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		outState?.putParcelable("STATE", viewModel.state.value)
		super.onSaveInstanceState(outState)
	}

	private fun initPresenter(viewModel: CompanyViewModel) {
		val maybePresenter = lastCustomNonConfigurationInstance as CompanyPresenterContract?

		if (maybePresenter != null) {
			companyPresenter = maybePresenter
		}

		if (!::companyPresenter.isInitialized) {
			companyPresenter = Injector.get().companyPresenter()
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
				hideFab(state.isFavorite)
			}
			state.errorType != ErrorType.NONE -> msvCompany.viewState = VIEW_STATE_ERROR
			state.contentChange == ContentChange.HIDE_FAB -> {
				hideFab(state.isFavorite)
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
		tvCompanyNatureOfBusiness.text = natureOfBusinessString
		tvCompanyIncorporated?.text = String.format(resources.getString(R.string.incorporated_on), company.dateOfCreation)
		tvCompanyAddressLine1?.text = company.registeredOfficeAddress?.addressLine1
		if (company.registeredOfficeAddress?.addressLine2 != null) {
			tvCompanyAddressLine2?.visibility = View.VISIBLE
			tvCompanyAddressLine2?.text = company.registeredOfficeAddress?.addressLine2
		}
		tvCompanyAddressPostalCode?.text = company.registeredOfficeAddress?.postalCode
		tvCompanyAddressLocality?.text = company.registeredOfficeAddress?.locality
		var formattedDate: String
		company.accounts?.lastAccounts?.madeUpTo?.let {
			val madeUpToDate = DateUtil.parseMySqlDate(it)
			madeUpToDate?.let { date ->
				formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(date))
				tvCompanyAccounts?.text = String.format(resources.getString(R.string.company_accounts_formatted_text), company.accounts?.lastAccounts?.type,
						formattedDate)
			}

		} ?: run {
			tvCompanyAccounts?.text = resources.getString(R.string.company_accounts_not_found)
		}
		company.annualReturn?.lastMadeUpTo?.let {
			val lastMadeUpToDate = DateUtil.parseMySqlDate(it)
			lastMadeUpToDate?.let { date ->
				formattedDate = DateUtil.formatShortDateFromTimeStampMillis(1000 * DateUtil.convertToTimestamp(date))
				tvCompanyAnnualReturns?.text = String.format(resources.getString(R.string.company_annual_returns_formatted_text), formattedDate)
			}
		} ?: run {
			tvCompanyAnnualReturns?.text = resources.getString(R.string.company_annual_returns_not_found)
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

	private fun hideFab(favorite: Boolean) {
		fabCompanyFavorite?.also {
			it.animate().cancel()
			it.animate().setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong()).scaleX(0f).scaleY(0f).alpha(0f)
					.setInterpolator(LinearOutSlowInInterpolator()).withEndAction { this.showFab(favorite) }
		}

	}

	override fun onBackPressed() {
		super.onBackPressed()
		overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out)
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		RxView.clicks(fabCompanyFavorite)
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe { companyPresenter.updateFavorites() }
		RxView.clicks(buttonShowOnMap)
				.`as`(AutoDispose.autoDisposable(this))
				.subscribe {
					val intent = Intent(this, MapActivity::class.java)
					intent.putExtra("addressString", viewModel.state.value.addressString)
					intent.putExtra("companyName", viewModel.state.value.companyName)
					startActivityWithRightSlide(intent)
				}
	}

	fun onFilingHistoryClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		startActivityWithRightSlide(createFilingHistoryIntent(viewModel.state.value.companyNumber))
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onChargesClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		startActivityWithRightSlide(createChargesIntent(viewModel.state.value.companyNumber))
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onInsolvencyClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		startActivityWithRightSlide(createInsolvencyIntent("214584654848484"))
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onOfficersClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		startActivityWithRightSlide(createOfficersIntent(viewModel.state.value.companyNumber))
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	fun onPersonsClicked(@Suppress("UNUSED_PARAMETER") view: View) {
		startActivityWithRightSlide(createPersonsIntent(viewModel.state.value.companyNumber))
		overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out)
	}

	//endregion
}

fun Context.createCompanyIntent(companyNumber: String, companyName: String): Intent {
	return Intent(this, CompanyActivity::class.java)
			.putExtra(COMPANY_NUMBER, companyNumber)
			.putExtra(COMPANY_NAME, companyName)
}
