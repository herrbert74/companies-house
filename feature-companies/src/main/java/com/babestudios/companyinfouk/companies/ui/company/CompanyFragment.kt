package com.babestudios.companyinfouk.companies.ui.company

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.babestudios.base.mvrx.BaseFragment
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.withState
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentCompanyBinding
import com.babestudios.companyinfouk.companies.ui.CompaniesActivity
import com.babestudios.companyinfouk.companies.ui.CompaniesState
import com.babestudios.companyinfouk.companies.ui.CompaniesViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable

class CompanyFragment : BaseFragment() {

	private val viewModel by existingViewModel(CompaniesViewModel::class)

	private val eventDisposables: CompositeDisposable = CompositeDisposable()

	private var _binding: FragmentCompanyBinding? = null
	private val binding get() = _binding!!

	//region life cycle

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentCompanyBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initializeUI()
	}

	private fun initializeUI() {
		viewModel.logScreenView(this::class.simpleName.orEmpty())
		(activity as AppCompatActivity).setSupportActionBar(binding.pabCompany.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabCompany.setNavigationOnClickListener { activity?.onBackPressed() }
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun orientationChanged() {
		val activity = requireActivity() as CompaniesActivity
		viewModel.setNavigator(activity.injectCompaniesNavigator())
	}

	//endregion

	//region render

	private fun selectSubscribes() {
		viewModel.selectSubscribe(CompaniesState::isFavorite) {
			hideFabToShowFavoriteState(it)
		}
	}

	override fun invalidate() {
		val tvMsvError = binding.msvCompany.findViewById<TextView>(R.id.tvMsvError)
		withState(viewModel) { state ->
			when (state.companyRequest) {
				is Loading -> {
					binding.msvCompany.viewState = VIEW_STATE_LOADING
					hideFabToShowFavoriteState(state.isFavorite)
				}
				is Fail -> {
					binding.msvCompany.viewState = VIEW_STATE_ERROR
					binding.fabCompanyFavorite.animate().cancel()
					binding.fabCompanyFavorite.hide()
					binding.pabCompany.setExpanded(false)
					tvMsvError.text = state.companyRequest.error.message
				}
				is Success -> {
					binding.msvCompany.viewState = VIEW_STATE_CONTENT
					showCompany(state.company)
				}
				else -> {}
			}
		}
	}

	private fun showCompany(company: Company) {
		binding.tvCompanyNumber.text = company.companyNumber
		binding.twoLineCompanyNatureOfBusiness.setTextSecond(company.natureOfBusiness)
		binding.tvCompanyIncorporated.text =
				String.format(resources.getString(R.string.incorporated_on), company.dateOfCreation)
		binding.addressViewCompany.setAddressLine1(company.registeredOfficeAddress.addressLine1)
		binding.addressViewCompany.setAddressLine2(company.registeredOfficeAddress.addressLine2)
		binding.addressViewCompany.setLocality(company.registeredOfficeAddress.locality)
		binding.addressViewCompany.setPostalCode(company.registeredOfficeAddress.postalCode)
		binding.addressViewCompany.setRegion(company.registeredOfficeAddress.region)
		binding.addressViewCompany.setCountry(company.registeredOfficeAddress.country)
		binding.twoLineCompanyAccounts.setTextSecond(company.lastAccountsMadeUpTo)

		if (!company.hasCharges) {
			binding.llCompanyCharges.visibility = GONE
		}
		if (!company.hasInsolvencyHistory) {
			binding.llCompanyInsolvency.visibility = GONE
		}
	}

	private fun showFab(favorite: Boolean) {
		binding.fabCompanyFavorite.also {
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
		binding.fabCompanyFavorite.also {
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
		RxView.clicks(binding.fabCompanyFavorite)
				.subscribe { viewModel.flipCompanyFavoriteState() }
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.addressViewCompany.getMapButton())
				.subscribe { viewModel.companiesNavigator.companyToMap() }
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.llCompanyFilings)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToFilings(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.llCompanyCharges)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToCharges(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.llCompanyInsolvency)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToInsolvencies(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.llCompanyOfficers)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToOfficers(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }
		RxView.clicks(binding.llCompanyPersons)
				.subscribe {
					withState(viewModel) { state ->
						viewModel.companiesNavigator.companyToPersons(state.companyNumber)
					}
				}
				.also { disposable -> eventDisposables.add(disposable) }

	}

	//endregion
}
