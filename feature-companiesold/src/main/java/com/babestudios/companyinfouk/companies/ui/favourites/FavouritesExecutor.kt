package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesItem
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesExecutor @Inject constructor(
	@MainDispatcher val mainContext: CoroutineDispatcher,
	@IoDispatcher val ioContext: CoroutineDispatcher,
	private val companiesRepository: CompaniesRepository,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	override fun executeAction(action: BootstrapIntent, getState: () -> State) {
		when (action) {
			is BootstrapIntent.LoadFavourites -> {
				companiesRepository.logScreenView("FavouritesFragment")
				loadFavourites()
			}
		}
	}

	override fun executeIntent(intent: Intent, getState: () -> State) {
		val showState = getState() as State.Show
		when (intent) {
			is Intent.FavouritesItemClicked -> favouritesItemClicked(intent.favouritesItem)

			is Intent.InitPendingRemoval -> {
				val favourites = showState.favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = true)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}

			is Intent.RemoveItem -> {
				companiesRepository.removeFavourite(intent.favouritesItem.searchHistoryItem)
				loadFavourites()
			}

			is Intent.CancelPendingRemoval -> {
				val favourites = showState.favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = false)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}
			is Intent.DeletedInCompany -> loadFavourites()
		}
	}

	private fun favouritesItemClicked(favouritesItem: FavouritesItem) {
		scope.launch {
			publish(
				SideEffect.FavouritesItemClicked(
					favouritesItem.searchHistoryItem.companyNumber,
					favouritesItem.searchHistoryItem.companyName,
				)
			)
		}
	}

	private fun loadFavourites() {
		scope.launch(ioContext) {
			val favourites = companiesRepository.favourites()
			withContext(mainContext) {
				dispatch(Message.LoadFavourites(favourites.map { FavouritesItem(it) }))
			}
		}
	}
}
