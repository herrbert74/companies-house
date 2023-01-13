package com.babestudios.companyinfouk.companies.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.companies.ui.company.CompanyScreen

import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesScreen

@Composable
fun CompaniesRootContent(component: CompaniesRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is CompaniesChild.Company -> CompanyScreen(child.component)
					is CompaniesChild.Favourites -> FavouritesScreen(child.component)
			}
		}
	}

}
