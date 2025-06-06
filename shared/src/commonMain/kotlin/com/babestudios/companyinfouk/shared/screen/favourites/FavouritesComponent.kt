package com.babestudios.companyinfouk.shared.screen.favourites

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.ext.asValue
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface FavouritesComp {

	fun onItemClicked(favouritesItem: FavouritesItem)

	fun onDeleteClicked(favouritesItem: FavouritesItem)

	fun onUndoDeleteClicked(favouritesItem: FavouritesItem)

	fun onBackClicked()

	fun goBack()

	val state: Value<FavouritesStore.State>

	val sideEffects: Flow<FavouritesStore.SideEffect>

	sealed class Output {
		data object Back : Output()
		data class Selected(val favouritesItem: FavouritesItem) : Output()
	}

}

class FavouritesComponent(
	componentContext: ComponentContext,
	val favouritesExecutor: FavouritesExecutor,
	private val output: FlowCollector<FavouritesComp.Output>,
) : FavouritesComp, ComponentContext by componentContext {

	private var favouritesStore: FavouritesStore =
		FavouritesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), favouritesExecutor).create()

	override fun onItemClicked(favouritesItem: FavouritesItem) {
		CoroutineScope(favouritesExecutor.mainContext).launch {
			output.emit(FavouritesComp.Output.Selected(favouritesItem = favouritesItem))
		}
	}

	override fun onDeleteClicked(favouritesItem: FavouritesItem) {
		favouritesStore.accept(FavouritesStore.Intent.InitPendingRemoval(favouritesItem))
	}

	override fun onUndoDeleteClicked(favouritesItem: FavouritesItem) {
		favouritesStore.accept(FavouritesStore.Intent.CancelPendingRemoval(favouritesItem))
	}

	override fun onBackClicked() {
		CoroutineScope(favouritesExecutor.mainContext).launch {
			favouritesStore.accept(FavouritesStore.Intent.ExpeditePendingRemovals)
		}
	}

	override fun goBack() {
		CoroutineScope(favouritesExecutor.mainContext).launch {
			output.emit(FavouritesComp.Output.Back)
			favouritesStore.dispose()
		}
	}

	override val state: Value<FavouritesStore.State>
		get() = favouritesStore.asValue()

	override val sideEffects: Flow<FavouritesStore.SideEffect>
		get() = favouritesStore.labels

}
