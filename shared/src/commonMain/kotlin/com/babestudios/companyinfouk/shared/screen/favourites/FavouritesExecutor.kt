package com.babestudios.companyinfouk.shared.screen.favourites

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PENDING_REMOVAL_TIMEOUT = 5000L // 5sec

class FavouritesExecutor constructor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, Nothing>(mainContext) {

	var removeJob: Job? = null

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
				startRemoveTimer(itemCopy)
			}

			is Intent.CancelPendingRemoval -> {
				removeJob?.cancel()
				val favourites = getState().favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = false)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}

			is Intent.DeletedInCompany -> fetchFavourites()
		}
	}

	private fun startRemoveTimer(favouritesItem: FavouritesItem) {
		removeJob = scope.launch(ioContext) {
			delay(PENDING_REMOVAL_TIMEOUT)
			removeFavourite(favouritesItem)
		}
	}

	private fun removeFavourite(favouritesItem: FavouritesItem) {
		companiesRepository.removeFavourite(favouritesItem.searchHistoryItem)
		fetchFavourites()
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
