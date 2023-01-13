package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesItem

interface FavouritesStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class FavouritesItemClicked(val favouritesItem: FavouritesItem) : Intent()
		data class InitPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
		data class RemoveItem(val favouritesItem: FavouritesItem) : Intent()
		data class CancelPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
		object DeletedInCompany : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val favourites: List<FavouritesItem>
		) : State()

		class Error(val t: Throwable) : State()

	}

}
