package com.babestudios.companyinfouk.officers.ui.details

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import io.reactivex.CompletableSource
import java.util.regex.Pattern
import javax.inject.Inject

interface OfficerDetailsPresenterContract : Presenter<OfficerDetailsState, OfficerDetailsViewModel> {
}

@SuppressLint("CheckResult")
class OfficerDetailsPresenter
@Inject
constructor(
		var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver
) : BasePresenter<OfficerDetailsState, OfficerDetailsViewModel>(
		schedulerProvider,
		errorResolver
), OfficerDetailsPresenterContract {

	override fun setViewModel(viewModel: OfficerDetailsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		val pattern = Pattern.compile("officers/(.+)/appointments")
		val matcher = pattern.matcher(viewModel.state.value?.officerItem?.links?.officer?.appointments)
		var officerId = ""
		if (matcher.find()) {
			officerId = matcher.group(1)
		}
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.OFFICER_ITEM_RECEIVED
				this.officerId = officerId
			}
		}

	}
}
