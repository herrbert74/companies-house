package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesItem

class FavouritesStoreFactory(
	private val storeFactory: StoreFactory,
	private val favouritesExecutor: FavouritesExecutor
) {

	fun create(): FavouritesStore =
		object : FavouritesStore, Store<Intent, State, SideEffect> by storeFactory.create(
			name = "FavouritesStore",
			initialState = State.Loading,
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
				is Message.LoadFavourites -> State.Show(msg.favouritesItems)
				is Message.UpdateFavourites -> State.Show(msg.favouritesItems)
			}
	}
}

sealed class BootstrapIntent {
	object LoadFavourites:BootstrapIntent()
}

sealed class Message {
	data class LoadFavourites(val favouritesItems: List<FavouritesItem>) : Message()
	data class UpdateFavourites(val favouritesItems: List<FavouritesItem>) : Message()
}

