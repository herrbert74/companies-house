package com.babestudios.companyinfouk.shared.screen.favourites

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.SideEffect
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val PENDING_REMOVAL_TIMEOUT = 5000L // 5sec

class FavouritesExecutor(
	private val companiesRepository: CompaniesRepository,
	val mainContext: CoroutineDispatcher,
	val ioContext: CoroutineDispatcher,
) : CoroutineExecutor<Intent, BootstrapIntent, State, Message, SideEffect>(mainContext) {

	private var removeJob: MutableMap<FavouritesItem, Job> = mutableMapOf()

	override fun executeAction(action: BootstrapIntent) {
		when (action) {
			is BootstrapIntent.LoadFavourites -> {
				companiesRepository.logScreenView("FavouritesFragment")
				fetchFavourites()
			}
		}
	}

	override fun executeIntent(intent: Intent) {
		when (intent) {
			is Intent.InitPendingRemoval -> {
				val favourites = state().favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = true)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
				startRemoveTimer(itemCopy)
			}

			is Intent.CancelPendingRemoval -> {
				removeJob[intent.favouritesItem]?.cancel()
				val favourites = state().favourites.toMutableList()
				val itemCopy = favourites[favourites.indexOf(intent.favouritesItem)].copy(isPendingRemoval = false)
				favourites[favourites.indexOf(intent.favouritesItem)] = itemCopy
				dispatch(Message.UpdateFavourites(favourites.toList()))
			}

			is Intent.ExpeditePendingRemovals -> scope.launch(ioContext) { expeditePendingRemovals() }

			is Intent.DeletedInCompany -> fetchFavourites()
		}
	}

	private fun startRemoveTimer(favouritesItem: FavouritesItem) {
		removeJob[favouritesItem] = scope.launch(ioContext) {
			delay(PENDING_REMOVAL_TIMEOUT)
			removeFavourite(favouritesItem)
		}
	}

	private suspend fun expeditePendingRemovals() {
		scope.launch(ioContext) {
			removeJob.entries.forEach { entry ->
				entry.value.cancel()
				companiesRepository.removeFavourite(entry.key.searchHistoryItem)
			}
		}.join()
		scope.launch(mainContext) { publish(SideEffect.Back) }
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
