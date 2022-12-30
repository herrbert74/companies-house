package com.babestudios.companyinfouk.filings.ui

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.util.MainDispatcher
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryComp
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryComponent
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryExecutor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.parcelize.Parcelize

import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsComponent
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsComp
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.util.IoDispatcher
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsExecutor

internal interface FilingsRootComp {
	val childStackValue: Value<ChildStack<*, FilingsChild>>
}

class FilingsRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val createFilingHistoryComp:
		(ComponentContext, FlowCollector<FilingHistoryComp.Output>) -> FilingHistoryComp,
	private val createFilingDetailsComp:
		(ComponentContext, FilingHistoryItem, FlowCollector<FilingDetailsComp.Output>) -> FilingDetailsComp,
) : FilingsRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		companiesRepository: CompaniesRepository,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		selectedCompanyId: String,
		finishHandler: () -> Unit, filingHistoryExecutor: FilingHistoryExecutor,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createFilingHistoryComp = { childContext, output ->
			FilingHistoryComponent(
				componentContext = childContext,
				selectedCompanyId = selectedCompanyId,
				filingHistoryExecutor = filingHistoryExecutor,
				output = output,
			)
		},
		createFilingDetailsComp = { childContext, filingHistoryItem, output ->
			FilingDetailsComponent(
				componentContext = childContext,
				filingHistoryItem = filingHistoryItem,
				filingDetailsExecutor = FilingDetailsExecutor(companiesRepository, mainContext, ioContext),
				output = output,
			)
		},
	)

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = {
			listOf(
				Configuration.FilingHistory
			)
		},
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): FilingsChild =
		when (configuration) {
			is Configuration.FilingHistory -> FilingsChild.FilingHistory(
				createFilingHistoryComp(componentContext, FlowCollector(::onFilingHistoryOutput))
			)
			is Configuration.FilingDetails -> FilingsChild.FilingDetails(
				createFilingDetailsComp(
					componentContext,
					configuration.filingHistoryItem,
					FlowCollector(::onFilingDetailsOutput)
				)
			)
		}

	private fun onFilingHistoryOutput(output: FilingHistoryComp.Output) = when (output) {
		is FilingHistoryComp.Output.Selected ->
			navigation.push(Configuration.FilingDetails(filingHistoryItem = output.filingHistoryItem))

		FilingHistoryComp.Output.Back -> finishHandler.invoke()

	}

	private fun onFilingDetailsOutput(output: FilingDetailsComp.Output) = when (output) {
		FilingDetailsComp.Output.Back -> navigation.pop()
	}
}

sealed class Configuration : Parcelable {
	@Parcelize
	object FilingHistory : Configuration()

	@Parcelize
	data class FilingDetails(val filingHistoryItem: FilingHistoryItem) : Configuration()
}

sealed class FilingsChild {
	data class FilingHistory(val component: FilingHistoryComp) : FilingsChild()
	data class FilingDetails(val component: FilingDetailsComp) : FilingsChild()
}
