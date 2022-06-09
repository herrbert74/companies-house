package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State
import com.babestudios.companyinfouk.companies.ui.favourites.list.FavouritesListItem

interface FavouritesStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class FavouritesItemClicked(val favouritesListItem: FavouritesListItem) : Intent()
		data class InitPendingRemoval(val favouritesListItem: FavouritesListItem) : Intent()
		data class RemoveItem(val favouritesListItem: FavouritesListItem) : Intent()
		data class CancelPendingRemoval(val favouritesListItem: FavouritesListItem) : Intent()
	}

	sealed class State {

		object Loading : State()

		class Show(
			val favourites: List<FavouritesListItem>
		) : State()

		class Error(val t: Throwable) : State()

	}

}
