package com.babestudios.companyinfouk.companies.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.base.coroutines.MainDispatcher
import com.babestudios.companyinfouk.companies.ui.company.CompanyComp
import com.babestudios.companyinfouk.companies.ui.company.CompanyComponent
import com.babestudios.companyinfouk.companies.ui.company.CompanyExecutor
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComp
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComponent
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

internal interface CompaniesRootComp {
	val childStackValue: Value<ChildStack<*, CompaniesChild>>
}

class CompaniesRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val companyName: String,
	private val companyId: String,
	private val createCompanyComp: (ComponentContext, FlowCollector<CompanyComp.Output>) -> CompanyComp,
	private val createFavouritesComp: (ComponentContext, FlowCollector<FavouritesComp.Output>) -> FavouritesComp,
) : CompaniesRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@MainDispatcher ioContext: CoroutineDispatcher,
		companiesRepository: CompaniesRepository,
		companyName: String,
		companyId: String,
		finishHandler: () -> Unit,
		companyExecutor: CompanyExecutor,
	) : this(
		componentContext = componentContext,
		finishHandler,
		companyName,
		companyId,
		createCompanyComp = { childContext, output ->
			CompanyComponent(
				componentContext = childContext,
				companyExecutor = companyExecutor,
				companyName = companyName,
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
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.Company(companyId, companyName)) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): CompaniesChild =
		when (configuration) {
			is Configuration.Company -> CompaniesChild.Company(
				createCompanyComp(componentContext, FlowCollector(::onCompanyOutput))
			)
			is Configuration.Favourites -> CompaniesChild.Favourites(
				createFavouritesComp(componentContext, FlowCollector(::onFavouritesOutput))
			)
		}

	private fun onCompanyOutput(output: CompanyComp.Output) = when (output) {
		is CompanyComp.Output.Selected -> {}//navigation.push(Configuration.TempPlaceHolder(name = output.name))

		CompanyComp.Output.Back -> finishHandler.invoke()
	}

	private fun onFavouritesOutput(output: FavouritesComp.Output) = when (output) {
		is FavouritesComp.Output.Selected ->
			navigation.push(
				Configuration.Company(
					output.favouritesItem.searchHistoryItem.companyNumber,
					output.favouritesItem.searchHistoryItem.companyName
				)
			)

		FavouritesComp.Output.Back -> finishHandler.invoke()
	}
}

sealed class Configuration : Parcelable {
	@Parcelize
	data class Company(val companyId: String, val companyName: String) : Configuration()

	@Parcelize
	object Favourites : Configuration()
}

sealed class CompaniesChild {
	data class Company(val component: CompanyComp) : CompaniesChild()
	data class Favourites(val component: FavouritesComp) : CompaniesChild()
}
