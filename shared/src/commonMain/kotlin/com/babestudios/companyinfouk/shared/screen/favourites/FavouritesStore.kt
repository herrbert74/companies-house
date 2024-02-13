package com.babestudios.companyinfouk.shared.screen.favourites

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.SideEffect
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.State

interface FavouritesStore : Store<Intent, State, SideEffect> {

	sealed class Intent {
		data class InitPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
		data class CancelPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
		data object ExpeditePendingRemovals : Intent()
		data object DeletedInCompany : Intent()
	}

	data class State(

		//result data
		val favourites: List<FavouritesItem> = listOf(),

		val error: Throwable? = null,

		//state
		val isLoading: Boolean = true,

		)

	sealed class SideEffect {
		data object Initial : SideEffect()
		data object Back : SideEffect()
	}

}
