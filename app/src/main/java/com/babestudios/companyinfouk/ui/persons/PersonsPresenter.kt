package com.babestudios.companyinfouk.ui.persons

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.base.rxjava.SingleObserverWrapper
import com.babestudios.companyinfo.data.BuildConfig
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.persons.Persons
import com.babestudios.companyinfouk.ui.persons.list.AbstractPersonsVisitable
import com.babestudios.companyinfouk.ui.persons.list.PersonsVisitable
import com.uber.autodispose.AutoDispose
import io.reactivex.CompletableSource
import javax.inject.Inject

interface PersonsPresenterContract : Presenter<PersonsState, PersonsViewModel> {
	fun fetchPersons(companyNumber: String)
	fun loadMorePersons(page: Int)
}

@SuppressLint("CheckResult")
class PersonsPresenter
@Inject
constructor(
		var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver
) : BasePresenter<PersonsState, PersonsViewModel>(
		schedulerProvider,
		errorResolver
), PersonsPresenterContract {

	override fun setViewModel(viewModel: PersonsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		viewModel.state.value?.persons?.let {
			sendToViewModel {
				it.apply {
					this.isLoading = false
					this.contentChange = ContentChange.PERSONS_RECEIVED
				}
			}
		} ?: run {
			sendToViewModel {
				it.apply {
					this.isLoading = true
				}
			}
			viewModel.state.value?.companyNumber?.also {
				fetchPersons(it)
			}
		}

	}

	override fun fetchPersons(companyNumber: String) {
		companiesRepository.getPersons(companyNumber, "0")
				.`as`(AutoDispose.autoDisposable(lifeCycleCompletable))
				.subscribeWith(object : SingleObserverWrapper<Persons>(this) {
					override fun onSuccess(reply: Persons) {
						sendToViewModel {
							it.apply {
								this.isLoading = false
								this.contentChange = ContentChange.PERSONS_RECEIVED
								this.persons = convertToVisitables(reply)
								this.totalCount = reply.totalResults?.toInt()
							}
						}
					}
				})
	}


	override fun loadMorePersons(page: Int) {
		if (viewModel.state.value?.persons == null || viewModel.state.value?.persons!!.size < viewModel.state.value?.totalCount!!) {
			companiesRepository.getPersons(viewModel.state.value?.companyNumber
					?: "", (page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE)).toString())
					.subscribeWith(object : SingleObserverWrapper<Persons>(this) {
						override fun onSuccess(reply: Persons) {
							val newList = viewModel.state.value?.persons?.toMutableList()
							newList?.addAll(convertToVisitables(reply))
							sendToViewModel {
								it.apply {
									this.isLoading = false
									this.contentChange = ContentChange.PERSONS_RECEIVED
									newList?.toList()?.let { list -> this.persons = list }
								}
							}
						}
					})
		}
	}

	private fun convertToVisitables(reply: Persons): List<AbstractPersonsVisitable> {
		return ArrayList(reply.items.map { item -> PersonsVisitable(item) })
	}

}