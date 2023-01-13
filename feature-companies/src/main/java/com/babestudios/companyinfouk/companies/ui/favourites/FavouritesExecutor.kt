package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesExecutor @Inject constructor(
	private val companiesRepository: CompaniesRepository,
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadFavourites -> {
				companiesRepository.logScreenView("FavouritesFragment")
				fetchFavourites()
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		when (intent) {
			is Intent.InitPendingRemoval -> {
				val favourites = getState().favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = true)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}

			is Intent.RemoveItem -> {
				companiesRepository.removeFavourite(intent.favouritesItem.searchHistoryItem)
				fetchFavourites()
			}

			is Intent.CancelPendingRemoval -> {
				val favourites = getState().favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = false)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}
			is Intent.DeletedInCompany -> fetchFavourites()
		}
	}

	//region favourites actions

	private fun fetchFavourites() {
		scope.launch(ioContext) {
			val favouritesResponse = companiesRepository.favourites()
			withContext(mainContext) {
				dispatch(Message.FavouritesMessage(favouritesResponse.map { FavouritesItem(it) }))
			}
		}
	}

	//endregion

}
