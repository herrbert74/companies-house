package com.babestudios.companyinfouk.charges.ui.charges

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.babestudios.companyinfouk.common.ext.asValue
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

interface ChargesComp {

	fun onItemClicked(chargesItem: ChargesItem)

	fun onLoadMore()

	fun onBackClicked()

	val state: Value<ChargesStore.State>

	sealed class Output {
		data class Selected(val chargesItem: ChargesItem) : Output()
		object Back : Output()
	}

}

class ChargesComponent(
	componentContext: ComponentContext,
	val chargesExecutor: ChargesExecutor,
	val companyNumber: String,
	private val output: FlowCollector<ChargesComp.Output>
) : ChargesComp, ComponentContext by componentContext {

	private var chargesStore: ChargesStore =
		ChargesStoreFactory(LoggingStoreFactory(DefaultStoreFactory()), chargesExecutor).create(companyNumber)

	override fun onItemClicked(chargesItem: ChargesItem) {
		CoroutineScope(chargesExecutor.mainContext).launch {
			output.emit(ChargesComp.Output.Selected(chargesItem = chargesItem))
		}
	}

	override fun onLoadMore() {
		chargesStore.accept(ChargesStore.Intent.LoadMoreCharges)
	}

	override fun onBackClicked() {
		CoroutineScope(chargesExecutor.mainContext).launch {
			output.emit(ChargesComp.Output.Back)
			chargesStore.dispose()
		}
	}

	override val state: Value<ChargesStore.State>
		get() = chargesStore.asValue()

}
