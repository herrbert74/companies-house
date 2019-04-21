package com.babestudios.companyinfouk.ui.charges

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfouk.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.charges.Charges
import com.babestudios.companyinfouk.ui.charges.list.AbstractChargesVisitable
import com.babestudios.companyinfouk.ui.charges.list.ChargesVisitable
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface ChargesPresenterContract : Presenter<ChargesState, ChargesViewModel> {
	fun fetchCharges(companyNumber: String)
	fun loadMoreCharges(page: Int)
}

@SuppressLint("CheckResult")
class ChargesPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<ChargesState, ChargesViewModel>(), ChargesPresenterContract {

	override fun setViewModel(viewModel: ChargesViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.chargeItems?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.CHARGES_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.companyNumber?.also {
				fetchCharges(it)
			}
		}
	}

	override fun fetchCharges(companyNumber: String) {
		companiesRepository.fetchCharges(companyNumber, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : SingleObserverWrapper<Charges>(this) {
					override fun onSuccess(reply: Charges) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.contentChange = ContentChange.CHARGES_RECEIVED
								this.chargeItems = convertToVisitables(reply)
								this.totalCount = reply.totalCount?.toInt()
							}
						}
					}
				})
	}

	private fun convertToVisitables(reply: Charges): List<AbstractChargesVisitable> {
		return ArrayList(reply.items.map { item -> ChargesVisitable(item) })
	}

	override fun loadMoreCharges(page: Int) {
		if (viewModel.state.value?.chargeItems == null || viewModel.state.value?.chargeItems!!.size < viewModel.state.value?.totalCount!!) {
			companiesRepository.fetchCharges(viewModel.state.value?.companyNumber
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : SingleObserverWrapper<Charges>(this) {
						override fun onSuccess(reply: Charges) {
							val newList = viewModel.state.value?.chargeItems?.toMutableList()
							newList?.addAll(convertToVisitables(reply))
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.CHARGES_RECEIVED
									newList?.toList()?.let { list -> this.chargeItems = list }
								}
							}
						}
					})
		}
	}

}