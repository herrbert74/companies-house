package com.babestudios.companyinfouk.shared.screen.company

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.shared.ext.asValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface CompanyComp {

	sealed class Output {
		data class Back(val isComingFromFavourites: Boolean, val isFavourite: Boolean) : Output()
		data class Map(val name: String, val address: String) : Output()
		data class Charges(val selectedCompanyId: String) : Output()
		data class Filings(val selectedCompanyId: String) : Output()
		data class Insolvencies(val selectedCompanyId: String) : Output()
		data class Officers(val selectedCompanyId: String) : Output()
		data class Persons(val selectedCompanyId: String) : Output()
	}

	val companyName: String

	val companyId: String

	fun onBackClicked(isFavourite: Boolean)

	fun onToggleFavouriteClicked()

	fun onMapClicked(addressString: String)

	fun onChargesClicked(selectedCompanyId: String)
	fun onFilingsClicked(selectedCompanyId: String)
	fun onInsolvenciesClicked(selectedCompanyId: String)
	fun onOfficersClicked(selectedCompanyId: String)
	fun onPersonsClicked(selectedCompanyId: String)

	val state: Value<CompanyStore.State>

}

@Suppress("unused")
class CompanyComponent(
	componentContext: ComponentContext,
	val companyExecutor: CompanyExecutor,
	override val companyName: String,
	override val companyId: String,
	val isComingFromFavourites: Boolean,
	private val output: FlowCollector<CompanyComp.Output>,
) : CompanyComp, ComponentContext by componentContext {

	private var companyStore =
		CompanyStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), companyExecutor).create(companyId)

	override fun onBackClicked(isFavourite: Boolean) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Back(isComingFromFavourites, isFavourite))
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

	override fun onChargesClicked(selectedCompanyId: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Charges(selectedCompanyId))
		}
	}

	override fun onFilingsClicked(selectedCompanyId: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Filings(selectedCompanyId))
		}
	}

	override fun onInsolvenciesClicked(selectedCompanyId: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Insolvencies(selectedCompanyId))
		}
	}

	override fun onOfficersClicked(selectedCompanyId: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Officers(selectedCompanyId))
		}
	}

	override fun onPersonsClicked(selectedCompanyId: String) {
		CoroutineScope(companyExecutor.mainContext).launch {
			output.emit(CompanyComp.Output.Persons(selectedCompanyId))
		}
	}

	override val state: Value<CompanyStore.State>
		get() = companyStore.asValue()

}
