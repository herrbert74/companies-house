package com.babestudios.companyinfouk.persons.ui.persons

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import kotlinx.parcelize.Parcelize

internal interface PersonsComp {
	val childStack: Value<ChildStack<*, PersonsChild>>
}

class PersonsComponent internal constructor(
	componentContext: ComponentContext
) : PersonsComp,
	ComponentContext by componentContext {

	private val navigation = StackNavigation<Configuration>()

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.Main) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStack = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): PersonsChild =
		when (configuration) {
			is Configuration.Main -> PersonsChild.Main(todoMain(componentContext, Consumer(::onMainOutput)))
			is Configuration.Edit -> PersonsChild.Edit(
				todoEdit(componentContext, configuration.itemId, Consumer(::onEditOutput))
			)
		}
}

sealed class Configuration : Parcelable {
	@Parcelize
	object Main : Configuration()

	@Parcelize
	data class Edit(val itemId: Long) : Configuration()
}

sealed class PersonsChild {
	data class Main(val component: TodoMain) : PersonsChild()
	data class Edit(val component: TodoEdit) : PersonsChild()
}
