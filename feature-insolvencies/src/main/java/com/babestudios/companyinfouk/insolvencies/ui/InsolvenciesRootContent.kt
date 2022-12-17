package com.babestudios.companyinfouk.insolvencies.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsListScreen
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesListScreen
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsScreen

@Composable
fun InsolvenciesRootContent(component: InsolvenciesRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme() {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is InsolvenciesChild.List -> InsolvenciesListScreen(child.component)
				is InsolvenciesChild.Details -> InsolvencyDetailsListScreen(child.component)
				is InsolvenciesChild.Practitioners -> PractitionerDetailsScreen(child.component)
			}
		}
	}

}
