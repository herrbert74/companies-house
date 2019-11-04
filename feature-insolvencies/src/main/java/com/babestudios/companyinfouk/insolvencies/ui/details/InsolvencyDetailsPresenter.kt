package com.babestudios.companyinfouk.insolvencies.ui.details

import android.annotation.SuppressLint
import android.content.Context
import com.babestudios.base.di.qualifier.ApplicationContext
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.insolvencies.ui.details.list.*
import com.babestudios.companyinfouk.ui.insolvencydetails.list.*
import io.reactivex.CompletableSource
import javax.inject.Inject

interface InsolvencyDetailsPresenterContract : Presenter<InsolvencyDetailsState, InsolvencyDetailsViewModel> {
}

@SuppressLint("CheckResult")
class InsolvencyDetailsPresenter
@Inject
constructor(
		var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver,
		@ApplicationContext val context: Context
) : BasePresenter<InsolvencyDetailsState, InsolvencyDetailsViewModel>(
		schedulerProvider,
		errorResolver
), InsolvencyDetailsPresenterContract {

	override fun setViewModel(viewModel: InsolvencyDetailsViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.INSOLVENCY_CASE_RECEIVED
				this.insolvencyDetailsItems = viewModel.state.value?.insolvencyCase?.let { insolvencyCase -> convertToVisitables(insolvencyCase) }
			}
		}
	}

	private fun convertToVisitables(insolvencyCase: InsolvencyCase): List<AbstractInsolvencyDetailsVisitable> {
		val list = ArrayList<AbstractInsolvencyDetailsVisitable>()
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(context.getText(R.string.insolvency_dates).toString())))
		for (item in insolvencyCase.dates) {
			list.add(InsolvencyDetailsDateVisitable(InsolvencyDetailsDateItem(item.date, item.type)))
		}
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(context.getText(R.string.insolvency_practitioners).toString())))
		for (item in insolvencyCase.practitioners) {
			list.add(InsolvencyDetailsPractitionerVisitable(InsolvencyDetailsPractitionerItem(item)))
		}
		return list
	}
}