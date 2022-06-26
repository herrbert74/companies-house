package com.babestudios.companyinfouk.companies.ui.company

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.babestudios.base.view.MultiStateView.VIEW_STATE_CONTENT
import com.babestudios.base.view.MultiStateView.VIEW_STATE_ERROR
import com.babestudios.base.view.MultiStateView.VIEW_STATE_LOADING
import com.babestudios.companyinfouk.common.ext.viewBinding
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.companies.databinding.FragmentCompanyBinding
import com.babestudios.companyinfouk.companies.ui.company.CompanyStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.DELETE_IN_COMPANY_BUNDLE_KEY
import com.babestudios.companyinfouk.companies.ui.favourites.DELETE_IN_COMPANY_KEY
import com.babestudios.companyinfouk.core.views.SingleLineView
import com.babestudios.companyinfouk.domain.model.company.Company
import com.babestudios.companyinfouk.navigation.NavigationFlow
import com.babestudios.companyinfouk.navigation.ToFlowNavigatable
import com.babestudios.companyinfouk.navigation.navigateSafe
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks

@AndroidEntryPoint
class CompanyFragment : Fragment(R.layout.fragment_company), MviView<State, UserIntent> {

	@Inject
	lateinit var companyViewModelFactory: CompanyViewModelFactory

	private val args: CompanyFragmentArgs by navArgs()

	private val viewModel: CompanyViewModel by viewModels {
		CompanyViewModel.provideFactory(
			companyViewModelFactory,
			args.number
		)
	}

	private val binding by viewBinding<FragmentCompanyBinding>()

	//region BaseMviView This is just a copy from com.arkivanov.mvikotlin.core.view.BaseMviView,
	// as we cannot use it here and do not want to use it separately. This part could be extracted into a delegate

	private val subject = PublishSubject<UserIntent>()

	override fun events(observer: Observer<UserIntent>): Disposable = subject.subscribe(observer)

	fun sideEffects(sideEffect: SideEffect) {
		when (sideEffect) {
			is SideEffect.MapClicked -> findNavController().navigateSafe(
				CompanyFragmentDirections.actionToMap(
					name = args.name,
					address = sideEffect.addressString
				)
			)
		}
	}

	/**
	 * Dispatches the provided `View Event` to all subscribers
	 *
	 * @param event a `View Event` to be dispatched
	 */
	@MainThread
	fun dispatch(event: UserIntent) {
		subject.onNext(event)
	}

	override fun render(model: State) {
		val tvMsvError = binding.msvCompany.findViewById<TextView>(R.id.tvMsvError)
		when (model) {
			is State.Loading -> {
				binding.msvCompany.viewState = VIEW_STATE_LOADING
				//hideFabToShowFavoriteState(state.isFavorite) TODO
			}
			is State.Error -> {
				binding.msvCompany.viewState = VIEW_STATE_ERROR
				binding.fabCompanyFavorite.animate().cancel()
				binding.fabCompanyFavorite.hide()
				binding.pabCompany.setExpanded(false)
				tvMsvError.text = model.t.message
			}
			is State.Show -> {
				binding.msvCompany.viewState = VIEW_STATE_CONTENT
				showCompany(model.company)
				hideFabToShowFavoriteState(model.isFavourite)
			}
		}
	}

	//endregion

	//region life cycle

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.onViewCreated(this, essentyLifecycle())
		initializeToolBar()
		setupEvents()
	}

	private fun initializeToolBar() {
		(activity as AppCompatActivity).setSupportActionBar(binding.pabCompany.getToolbar())
		val toolBar = (activity as AppCompatActivity).supportActionBar
		toolBar?.setDisplayHomeAsUpEnabled(true)
		binding.pabCompany.setNavigationOnClickListener { activity?.onBackPressed() }
		toolBar?.title = args.name
	}

	private fun setupEvents() {
		binding.fabCompanyFavorite.clicks().onEach {
			dispatch(UserIntent.FabFavouritesClicked)
		}.launchIn(lifecycleScope)

		binding.addressViewCompany.getMapButton().clicks().onEach {
			dispatch(UserIntent.MapClicked)
		}.launchIn(lifecycleScope)

		navigateToFlow(binding.llCompanyCharges, NavigationFlow.ChargesFlow(args.number))
		navigateToFlow(binding.llCompanyFilings, NavigationFlow.FilingsFlow(args.number))
		navigateToFlow(binding.llCompanyInsolvency, NavigationFlow.InsolvenciesFlow(args.number))
		navigateToFlow(binding.llCompanyOfficers, NavigationFlow.OfficersFlow(args.number))
		navigateToFlow(binding.llCompanyPersons, NavigationFlow.PersonsFlow(args.number))
	}

	private fun navigateToFlow(singleLineView: SingleLineView, navigationFlow: NavigationFlow) {
		singleLineView.clicks().onEach {
			(requireActivity() as ToFlowNavigatable).navigateToFlow(navigationFlow)
		}.launchIn(lifecycleScope)
	}

	//endregion

	//region render

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
		setFragmentResult(DELETE_IN_COMPANY_KEY, bundleOf(DELETE_IN_COMPANY_BUNDLE_KEY to !favorite))
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
}

sealed class UserIntent {
	object FabFavouritesClicked : UserIntent()
	object MapClicked : UserIntent()
}

sealed class SideEffect {
	data class MapClicked(val addressString: String) : SideEffect()
}
