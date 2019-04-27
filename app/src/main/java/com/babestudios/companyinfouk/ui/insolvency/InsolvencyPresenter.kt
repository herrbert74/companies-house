package com.babestudios.companyinfouk.ui.insolvency

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency
import com.babestudios.companyinfouk.ui.insolvency.list.InsolvencyVisitable
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface InsolvencyPresenterContract : Presenter<InsolvencyState, InsolvencyViewModel> {
	fun fetchInsolvencies(companyNumber: String)
}

@SuppressLint("CheckResult")
class InsolvencyPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository, schedulerProvider: SchedulerProvider)
	: BasePresenter<InsolvencyState, InsolvencyViewModel>(schedulerProvider), InsolvencyPresenterContract {

	override fun setViewModel(viewModel: InsolvencyViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.insolvencyItems?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.INSOLVENCIES_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.companyNumber?.also {
				fetchInsolvencies(it)
			}
		}
	}

	override fun fetchInsolvencies(companyNumber: String) {
		companiesRepository.getInsolvency(companyNumber)
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : SingleObserverWrapper<Insolvency>(this) {
					override fun onSuccess(reply: Insolvency) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.contentChange = ContentChange.INSOLVENCIES_RECEIVED
								this.insolvencyItems = convertToVisitables(reply)
							}
						}
					}
				})
	}

	private fun convertToVisitables(reply: Insolvency): List<InsolvencyVisitable> {
		return ArrayList(reply.cases.map { insolvencyCase -> InsolvencyVisitable(insolvencyCase) })
	}
}