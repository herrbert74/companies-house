package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State

class FavouritesStoreFactory(
	private val storeFactory: StoreFactory,
	private val favouritesExecutor: FavouritesExecutor,
) {

	fun create(): FavouritesStore =
		object : FavouritesStore, Store<Intent, State, Nothing> by storeFactory.create(
			name = "FavouritesStore",
			initialState = State(),
			bootstrapper = FavouritesBootstrapper(),
			executorFactory = { favouritesExecutor },
			reducer = FavouritesReducer
		) {}

	private class FavouritesBootstrapper : CoroutineBootstrapper<BootstrapIntent>() {
		override fun invoke() {
			dispatch(BootstrapIntent.LoadFavourites)
		}
	}

	private object FavouritesReducer : Reducer<State, Message> {
		override fun State.reduce(msg: Message): State =
			when (msg) {
				is Message.FavouritesMessage -> copy(isLoading = false, favourites = msg.favouritesItems)
				is Message.UpdateFavourites -> copy(isLoading = false, favourites = msg.favouritesItems)
			}
	}

}

sealed class Message {
	data class FavouritesMessage(val favouritesItems: List<FavouritesItem>) : Message()
	data class UpdateFavourites(val favouritesItems: List<FavouritesItem>) : Message()
}

sealed class BootstrapIntent {
	object LoadFavourites : BootstrapIntent()
}
