package com.babestudios.companyinfouk.companies.ui.favourites

import android.annotation.SuppressLint
import com.babestudios.base.mvp.BasePresenter
import com.babestudios.base.mvp.Presenter
import com.babestudios.base.rxjava.ErrorResolver
import com.babestudios.base.rxjava.SchedulerProvider
import com.babestudios.companyinfouk.data.CompaniesRepositoryContract
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.ui.favourites.list.FavouritesItem
import com.babestudios.companyinfouk.ui.favourites.list.FavouritesVisitable
import io.reactivex.CompletableSource
import javax.inject.Inject

interface FavouritesPresenterContract : Presenter<FavouritesState, FavouritesViewModel> {
	var companiesRepository: CompaniesRepositoryContract
	fun removeFavourite(favouriteToRemove: SearchHistoryItem)
	fun loadFavourites()
}

@SuppressLint("CheckResult")
class FavouritesPresenter
@Inject
constructor(
		override var companiesRepository: CompaniesRepositoryContract,
		schedulerProvider: SchedulerProvider,
		errorResolver: ErrorResolver
) : BasePresenter<FavouritesState, FavouritesViewModel>(
		schedulerProvider,
		errorResolver
), FavouritesPresenterContract {

	override fun setViewModel(viewModel: FavouritesViewModel, lifeCycleCompletable: CompletableSource?) {
		this.viewModel = viewModel
		this.lifeCycleCompletable = lifeCycleCompletable
		loadFavourites()
	}

	override fun loadFavourites() {
		sendToViewModel {
			it.apply {
				this.contentChange = ContentChange.FAVOURITES_RECEIVED
				this.favouriteItems = convertToVisitables(companiesRepository.favourites)
			}
		}
	}

	private fun convertToVisitables(favourites: Array<SearchHistoryItem>): List<FavouritesVisitable> {
		return ArrayList(favourites.map { item -> FavouritesVisitable(FavouritesItem(item)) })
	}

	override fun removeFavourite(favouriteToRemove: SearchHistoryItem) {
		companiesRepository.removeFavourite(favouriteToRemove)
	}

}