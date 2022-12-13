package com.babestudios.companyinfouk.charges.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.charges.ui.charges.ChargesListScreen
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsListScreen
import com.babestudios.companyinfouk.design.CompaniesTheme

@Composable
fun ChargesRootContent(component: ChargesRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme() {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is ChargesChild.List -> ChargesListScreen(child.component)
				is ChargesChild.Details -> ChargeDetailsListScreen(child.component)
			}
		}
	}

}
