package com.babestudios.companyinfouk.filings.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.design.CompaniesTheme

import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryScreen

import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsScreen

@Composable
fun FilingsRootContent(component: FilingsRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme() {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
					is FilingsChild.FilingHistory -> FilingHistoryScreen(child.component)
					is FilingsChild.FilingDetails -> FilingDetailsScreen(child.component)
			}
		}
	}

}
