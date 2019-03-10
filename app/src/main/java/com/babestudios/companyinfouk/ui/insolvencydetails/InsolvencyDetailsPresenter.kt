package com.babestudios.companyinfouk.ui.insolvencydetails

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.companyinfouk.CompaniesHouseApplication
import com.babestudios.companyinfouk.R
import com.babestudios.companyinfouk.data.CompaniesRepository
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.ui.insolvencydetails.list.*
import io.reactivex.CompletableSource
import javax.inject.Inject

interface InsolvencyDetailsPresenterContract : Presenter<InsolvencyDetailsState, InsolvencyDetailsViewModel> {
}

@SuppressLint("CheckResult")
class InsolvencyDetailsPresenter
@Inject
constructor(var companiesRepository: CompaniesRepository) : BasePresenter<InsolvencyDetailsState, InsolvencyDetailsViewModel>(), InsolvencyDetailsPresenterContract {

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
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(CompaniesHouseApplication.context.getText(R.string.insolvency_dates).toString())))
		for (item in insolvencyCase.dates) {
			list.add(InsolvencyDetailsDateVisitable(InsolvencyDetailsDateItem(item.date, item.type)))
		}
		list.add(InsolvencyDetailsTitleVisitable(InsolvencyDetailsTitleItem(CompaniesHouseApplication.context.getText(R.string.insolvency_practitioners).toString())))
		for (item in insolvencyCase.practitioners) {
			list.add(InsolvencyDetailsPractitionerVisitable(InsolvencyDetailsPractitionerItem(item)))
		}
		return list
	}
}