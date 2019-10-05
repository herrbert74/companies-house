package com.babestudios.companyinfouk.ui.persondetails

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import io.reactivex.CompletableSource
import javax.inject.Inject

interface PersonDetailsPresenterContract : Presenter<PersonDetailsState, PersonDetailsViewModel> {
}

@SuppressLint("CheckResult")
class PersonDetailsPresenter
@Inject
constructor(
		var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver
) : BasePresenter<PersonDetailsState, PersonDetailsViewModel>(
		schedulerProvider,
		errorResolver
), PersonDetailsPresenterContract {

	override fun setViewModel(viewModel: PersonDetailsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.PERSON_ITEM_RECEIVED
			}
		}

	}
}