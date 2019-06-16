package com.babestudios.companyinfouk.ui.company

import android.annotation.SuppressLint
import com.babestudios.base.ext.biLet
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.company.Company
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import io.reactivex.CompletableSource
import javax.inject.Inject

interface CompanyPresenterContract : Presenter<CompanyState, CompanyViewModel> {
	fun fetchCompany(companyNumber: String)
	fun updateFavorites()
}

@SuppressLint("CheckResult")
class CompanyPresenter
@Inject
constructor(var companiesRepository: CompaniesRepositoryContract, schedulerProvider: SchedulerProvider) : BasePresenter<CompanyState, CompanyViewModel>(schedulerProvider), CompanyPresenterContract {

	override fun setViewModel(viewModel: CompanyViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.company?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.COMPANY_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
					this.isFavorite = companiesRepository.isFavourite(SearchHistoryItem(this.companyName, this.companyNumber, 0))
				}
			}
			viewModel.state.value?.companyNumber?.also {
				fetchCompany(it)
			}
		}
	}

	override fun fetchCompany(companyNumber: String) {
		companiesRepository.getCompany(companyNumber)
				.subscribeWith(object : SingleObserverWrapper<Company>(this) {
					override fun onSuccess(reply: Company) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.contentChange = ContentChange.COMPANY_RECEIVED
								this.company = reply
								this.company?.links?.self
								this.addressString = getAddressString(reply)
								if (reply.sicCodes.isNotEmpty()) {
									this.natureOfBusinessString = "${reply.sicCodes[0]} ${companiesRepository.sicLookup(reply.sicCodes[0])}"
								} else {
									//TODO Create a string provider to get this from strings.xml, but don't rely on context here
									this.natureOfBusinessString = "No data"
								}
							}
						}
					}
				})
	}

	private fun getAddressString(company: Company): String {
		return company.registeredOfficeAddress?.addressLine2?.let { line2 ->
			line2
		} ?: run {
			(company.registeredOfficeAddress?.addressLine1
					+ ", "
					+ company.registeredOfficeAddress?.locality
					+ ", "
					+ company.registeredOfficeAddress?.postalCode)
		}
	}

	override fun updateFavorites() {
		(viewModel.state.value?.companyName to viewModel.state.value?.companyNumber).biLet { companyName, companyNumber ->
			if (companiesRepository.isFavourite(SearchHistoryItem(companyName, companyNumber, 0))) {
				companiesRepository.removeFavourite(SearchHistoryItem(companyName, companyNumber, 0))
			} else {
				companiesRepository.addFavourite(SearchHistoryItem(companyName, companyNumber, 0))
			}
			sendToViewModel {
				it.apply {
					this.contentChange = ContentChange.HIDE_FAB
					this.isFavorite = companiesRepository.isFavourite(SearchHistoryItem(this.companyName, this.companyNumber, 0))
				}
			}
		}
	}

}