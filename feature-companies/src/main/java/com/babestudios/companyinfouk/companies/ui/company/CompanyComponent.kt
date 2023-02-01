package com.babestudios.companyinfouk.companies.ui.company

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.companies.ui.Configuration
import com.babestudios.companyinfouk.navigation.NavigationFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface CompanyComp {

	sealed class Output {
		data class Back(val previousConfig: Configuration?, val isFavourite: Boolean) : Output()
		data class NavigateTo(val navigationFlow: NavigationFlow) : Output()
		data class Map(val name: String, val address: String) : Output()
	}

	val companyName: String

	val companyId: String

	val previousConfig: Configuration

	fun onBackClicked(previousConfig: Configuration, isFavourite: Boolean)

	fun onToggleFavouriteClicked()

	fun onMapClicked(addressString: String)

	fun onDeepLinkClicked(navigationFlow: NavigationFlow)

	val state: Value<CompanyStore.State>

}

@Suppress("unused")
class CompanyComponent(
	componentContext: ComponentContext,
	val companyExecutor: CompanyExecutor,
	override val companyName: String,
	override val companyId: String,
	override val previousConfig: Configuration,
	private val output: FlowCollector<CompanyComp.Output>,
) : CompanyComp, ComponentContext by componentContext {

	private var companyStore =
		CompanyStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), companyExecutor).create(companyId, previousConfig)

	override fun onBackClicked(previousConfig: Configuration, isFavourite: Boolean) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Back(previousConfig, isFavourite))
		}
	}

	override fun onToggleFavouriteClicked() {
		CoroutineScope(companyExecutor.mainContext).launch {
			companyStore.accept(CompanyStore.Intent.FabFavouritesClicked)
		}
	}

	override fun onMapClicked(addressString: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Map(companyName, addressString))
		}
	}

	override fun onDeepLinkClicked(navigationFlow: NavigationFlow) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.NavigateTo(navigationFlow))
		}
	}

	override val state: Value<CompanyStore.State>
		get() = companyStore.asValue()

}
