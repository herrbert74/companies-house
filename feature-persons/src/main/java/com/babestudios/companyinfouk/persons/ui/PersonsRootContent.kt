package com.babestudios.companyinfouk.persons.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsScreen
import com.babestudios.companyinfouk.persons.ui.persons.PersonsListScreen

@Composable
fun PersonsRootContent(component: PersonsRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme() {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is PersonsChild.List -> PersonsListScreen(child.component)
				is PersonsChild.Details -> PersonDetailsScreen(child.component)
			}
		}
	}

}
