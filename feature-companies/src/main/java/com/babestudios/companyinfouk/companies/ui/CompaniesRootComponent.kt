package com.babestudios.companyinfouk.companies.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.backStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.base.coroutines.MainDispatcher
import com.babestudios.companyinfouk.companies.ui.company.CompanyComp
import com.babestudios.companyinfouk.companies.ui.company.CompanyComponent
import com.babestudios.companyinfouk.companies.ui.company.CompanyExecutor
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComp
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComponent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.companies.ui.main.MainComp
import com.babestudios.companyinfouk.companies.ui.main.MainComponent
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.companies.ui.map.MapComp
import com.babestudios.companyinfouk.companies.ui.map.MapComponent
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyComp
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyComponent
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.navigation.NavigationFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface CompaniesRootComp {
	val childStackValue: Value<ChildStack<*, CompaniesChild>>
}

class CompaniesRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val createCompanyComp: (
		ComponentContext, String, String, Configuration, FlowCollector<CompanyComp.Output>,
	) -> CompanyComp,
	private val createFavouritesComp: (ComponentContext, FlowCollector<FavouritesComp.Output>) -> FavouritesComp,
	private val createMainComp: (ComponentContext, FlowCollector<MainComp.Output>) -> MainComp,
	private val createMapComp: (ComponentContext, String, String, FlowCollector<MapComp.Output>) -> MapComp,
	private val createPrivacyComp: (ComponentContext, FlowCollector<PrivacyComp.Output>) -> PrivacyComp,
	private val navigateToFlow: (NavigationFlow) -> Unit,
	private val startConfiguration: Configuration,
	private val popBackStack: () -> Unit,
) : CompaniesRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		companiesRepository: CompaniesRepository,
		finishHandler: () -> Unit,
		mainExecutor: MainExecutor,
		navigateToFlow: (NavigationFlow) -> Unit,
		startConfiguration: Configuration = Configuration.Main,
		popBackStack: () -> Unit,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createCompanyComp = { childContext, companyName, companyId, previousConfig, output ->
			CompanyComponent(
				componentContext = childContext,
				companyExecutor = CompanyExecutor(companiesRepository, mainContext, ioContext),
				companyName = companyName,
				previousConfig = previousConfig,
				companyId = companyId,
				output = output,
			)
		},
		createFavouritesComp = { childContext, output ->
			FavouritesComponent(
				componentContext = childContext,
				favouritesExecutor = FavouritesExecutor(companiesRepository, mainContext, ioContext),
				output = output,
			)
		},
		createMainComp = { childContext, output ->
			MainComponent(
				componentContext = childContext,
				mainExecutor = mainExecutor,
				output = output,
			)
		},
		createMapComp = { childContext, name, address, output ->
			MapComponent(
				componentContext = childContext,
				mainContext = mainContext,
				name = name,
				address = address,
				output = output,
			)
		},
		createPrivacyComp = { childContext, output ->
			PrivacyComponent(
				componentContext = childContext,
				mainContext = mainContext,
				output = output,
			)
		},
		navigateToFlow,
		startConfiguration,
		popBackStack
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(startConfiguration) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): CompaniesChild =
		when (configuration) {
			is Configuration.Main -> CompaniesChild.Main(
				createMainComp(componentContext, FlowCollector(::onMainOutput))
			)

			is Configuration.Company -> CompaniesChild.Company(
				createCompanyComp(
					componentContext,
					configuration.companyName,
					configuration.companyId,
					configuration.previousConfig,
					FlowCollector(::onCompanyOutput)
				)
			)

			is Configuration.Favourites -> CompaniesChild.Favourites(
				createFavouritesComp(componentContext, FlowCollector(::onFavouritesOutput))
			)

			is Configuration.Map -> CompaniesChild.Map(
				createMapComp(componentContext, configuration.name, configuration.address, FlowCollector(::onMapOutput))
			)

			is Configuration.Privacy -> CompaniesChild.Privacy(
				createPrivacyComp(componentContext, FlowCollector(::onPrivacyOutput))
			)
		}

	private fun onCompanyOutput(output: CompanyComp.Output) = when (output) {
		is CompanyComp.Output.NavigateTo -> navigateToFlow(output.navigationFlow)
		is CompanyComp.Output.Map -> navigation.push(Configuration.Map(output.name, output.address))

		is CompanyComp.Output.Back -> {
			if (childStackValue.backStack.isEmpty()) {
				popBackStack()
			} else {
				navigation.pop {
					if (goingToFavouritesAndRemovedFavourite(output)) {
						navigation.pop()
						navigation.push(Configuration.Favourites) //replaceCurrent does not work here!
					}
				}
			}

		}
	}

	private fun goingToFavouritesAndRemovedFavourite(output: CompanyComp.Output.Back) =
		output.previousConfig is Configuration.Favourites && !output.isFavourite

	private fun onFavouritesOutput(output: FavouritesComp.Output) = when (output) {
		is FavouritesComp.Output.Selected ->
			navigation.push(
				Configuration.Company(
					output.favouritesItem.searchHistoryItem.companyNumber,
					output.favouritesItem.searchHistoryItem.companyName,
					Configuration.Favourites
				)
			)

		FavouritesComp.Output.Back -> navigation.pop()
	}

	private fun onMainOutput(output: MainComp.Output) = when (output) {
		is MainComp.Output.Selected ->
			navigation.push(
				Configuration.Company(
					output.companySearchResultItem.companyNumber!!,
					output.companySearchResultItem.title!!,
					Configuration.Main,
				)
			)

		is MainComp.Output.Privacy -> navigation.push(Configuration.Privacy)

		is MainComp.Output.RecentSearchHistorySelected -> {
			navigation.push(
				Configuration.Company(
					output.searchHistoryItem.companyNumber,
					output.searchHistoryItem.companyName,
					Configuration.Main
				)
			)
		}

		MainComp.Output.Back -> finishHandler.invoke()

		MainComp.Output.Favourites -> navigation.push(Configuration.Favourites)
	}

	private fun onMapOutput(output: MapComp.Output) = when (output) {
		MapComp.Output.Back ->
			if (childStackValue.backStack.isEmpty()) {
				popBackStack()
			} else {
				navigation.pop()
			}
	}

	private fun onPrivacyOutput(output: PrivacyComp.Output) = when (output) {
		PrivacyComp.Output.Back -> navigation.pop()
	}
}

sealed class Configuration : Parcelable {

	@Parcelize
	data class Company(val companyId: String, val companyName: String, val previousConfig: Configuration) :
		Configuration()

	@Parcelize
	object Favourites : Configuration()

	@Parcelize
	object Main : Configuration()

	@Parcelize
	data class Map(val name: String, val address: String) : Configuration()

	@Parcelize
	object Privacy : Configuration()

}

sealed class CompaniesChild {
	data class Company(val component: CompanyComp) : CompaniesChild()
	data class Favourites(val component: FavouritesComp) : CompaniesChild()
	data class Main(val component: MainComp) : CompaniesChild()
	data class Map(val component: MapComp) : CompaniesChild()
	data class Privacy(val component: PrivacyComp) : CompaniesChild()
}
