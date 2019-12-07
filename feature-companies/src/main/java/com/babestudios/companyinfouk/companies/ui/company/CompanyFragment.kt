package com.babestudios.companyinfouk.companies.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.airbnb.mvrx.*
import com.babestudios.base.ext.convertToTimestamp
import com.babestudios.base.ext.formatShortDateFromTimeStampMillis
import com.babestudios.base.ext.parseMySqlDate
import com.babestudios.base.view.MultiStateView.*
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.babestudios.companyinfouk.data.model.company.Company
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_company.*

class CompanyFragment : BaseMvRxFragment() {

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	//region life cycle

	override fun onCreateView(
			inflater: LayoutInflater, container: ViewGroup?,
			savedInstanceState: Bundle?
	): View {

		return inflater.inflate(R.layout.fragment_company, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(pabCompany.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		pabCompany.setNavigationOnClickListener { activity?.onBackPressed() }
		withState(viewModel) {
			toolBar?.title = it.companyName
			viewModel.fetchCompany(it.companyNumber)
		}
		selectSubscribes()
	}

	override fun onResume() {
		super.onResume()
		observeActions()
	}

	//endregion

	//region render

	private fun selectSubscribes() {
		viewModel.selectSubscribe(CompaniesState::isFavorite) {
			hideFabToShowFavoriteState(it)
		}
	}

	override fun invalidate() {
		val tvMsvError = msvCompany.findViewById<TextView>(R.id.tvMsvError)
		withState(viewModel) { state ->
			when (state.companyRequest) {
				is Loading -> {
					msvCompany.viewState = VIEW_STATE_LOADING
					hideFabToShowFavoriteState(state.isFavorite)
				}
				is Fail -> {
					msvCompany.viewState = VIEW_STATE_ERROR
					fabCompanyFavorite.animate().cancel()
					fabCompanyFavorite.hide()
					pabCompany.setExpanded(false)
					tvMsvError.text = state.companyRequest.error.message
				}
				is Success -> {
					msvCompany.viewState = VIEW_STATE_CONTENT
					showCompany(state.company, state.natureOfBusinessString)
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
				@Suppress("MagicNumber")
				formattedDate = (1000 * date.convertToTimestamp()).formatShortDateFromTimeStampMillis()
				lblCompanyAccounts?.text = String.format(
						resources.getString(R.string.company_accounts_formatted_text),
						company.accounts?.lastAccounts?.type,
						formattedDate
				)
			}

		} ?: run {
			lblCompanyAccounts?.text = resources.getString(R.string.company_accounts_not_found)
		}
		company.annualReturn?.lastMadeUpTo?.let {
			val lastMadeUpToDate = it.parseMySqlDate()
			lastMadeUpToDate?.let { date ->
				@Suppress("MagicNumber")
				formattedDate = (1000 * date.convertToTimestamp()).formatShortDateFromTimeStampMillis()
				lblCompanyAnnualReturns?.text = String.format(
						resources.getString(R.string.company_annual_returns_formatted_text),
						formattedDate
				)
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
			it.animate()
					.setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong())
					.scaleX(1f)
					.scaleY(1f)
					.alpha(1f)
					.interpolator = LinearOutSlowInInterpolator()
		}
	}

	private fun hideFabToShowFavoriteState(favorite: Boolean) {
		fabCompanyFavorite?.also {
			it.animate().cancel()
			it.animate()
					.setDuration(resources.getInteger(R.integer.fab_move_in_duration).toLong())
					.scaleX(0f)
					.scaleY(0f)
					.alpha(0f)
					.setInterpolator(LinearOutSlowInInterpolator())
					.withEndAction { this.showFab(favorite) }
		}
	}

	//endregion

	//region events

	private fun observeActions() {
		eventDisposables.clear()
		RxView.clicks(fabCompanyFavorite)
				.subscribe { viewModel.flipCompanyFavoriteState() }
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(btnShowOnMap)
				.subscribe { viewModel.companiesNavigator.companyToMap() }
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(llCompanyFilings)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToFilings(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(llCompanyCharges)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToCharges(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(llCompanyInsolvency)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToInsolvencies(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(llCompanyOfficers)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToOfficers(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(llCompanyPersons)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToPersons(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }

	}

	//endregion
}
