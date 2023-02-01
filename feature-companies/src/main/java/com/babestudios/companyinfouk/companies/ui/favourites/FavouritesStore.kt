package com.babestudios.companyinfouk.companies.ui.favourites

import com.arkivanov.mvikotlin.core.store.Store
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.Intent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesStore.State

interface FavouritesStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class InitPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
        data class CancelPendingRemoval(val favouritesItem: FavouritesItem) : Intent()
        object DeletedInCompany : Intent()
    }

    data class State (

        //result data
        val favourites: List<FavouritesItem> = listOf(),

        val error: Throwable? = null,

        //state
        val isLoading: Boolean = true

    )

}
