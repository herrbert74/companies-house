package com.babestudios.companyinfouk.ui.officers

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfo.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.officers.Officers
import com.babestudios.companyinfouk.ui.officers.list.AbstractOfficersVisitable
import com.babestudios.companyinfouk.ui.officers.list.OfficersVisitable
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface OfficersPresenterContract : Presenter<OfficersState, OfficersViewModel> {
	fun fetchOfficers(companyNumber: String)
	fun loadMoreOfficers(page: Int)
}

@SuppressLint("CheckResult")
class OfficersPresenter
@Inject
constructor(
		var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver
) : BasePresenter<OfficersState, OfficersViewModel>(
		schedulerProvider,
		errorResolver
), OfficersPresenterContract {

	override fun setViewModel(viewModel: OfficersViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.officerItems?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.OFFICERS_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.companyNumber?.also {
				fetchOfficers(it)
			}
		}
	}

	override fun fetchOfficers(companyNumber: String) {
		companiesRepository.getOfficers(companyNumber, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : SingleObserverWrapper<Officers>(this) {
					override fun onSuccess(reply: Officers) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.contentChange = ContentChange.OFFICERS_RECEIVED
								this.officerItems = convertToVisitables(reply)
								this.totalCount = reply.totalResults
							}
						}
					}
				})
	}


	override fun loadMoreOfficers(page: Int) {
		if (viewModel.state.value?.officerItems == null || viewModel.state.value?.officerItems!!.size < viewModel.state.value?.totalCount!!) {
			companiesRepository.getOfficers(viewModel.state.value?.companyNumber
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : SingleObserverWrapper<Officers>(this) {
						override fun onSuccess(reply: Officers) {
							val newList = viewModel.state.value?.officerItems?.toMutableList()
							newList?.addAll(convertToVisitables(reply))
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.OFFICERS_RECEIVED
									newList?.toList()?.let { list -> this.officerItems = list }
								}
							}
						}
					})
		}
	}

	private fun convertToVisitables(reply: Officers): List<AbstractOfficersVisitable> {
		return ArrayList(reply.items.map { item -> OfficersVisitable(item) })
	}

}