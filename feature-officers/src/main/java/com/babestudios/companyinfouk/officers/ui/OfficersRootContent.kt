package com.babestudios.companyinfouk.officers.ui

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsListScreen
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsScreen
import com.babestudios.companyinfouk.officers.ui.officers.OfficersListScreen

@Composable
fun OfficersRootContent(component: OfficersRootComponent) {

	val stack = component.childStackValue

	CompaniesTheme() {
		Children(stack = stack, animation = stackAnimation(scale())) {
			when (val child = it.instance) {
				is OfficersChild.List -> OfficersListScreen(child.component)
				is OfficersChild.Details -> OfficerDetailsScreen(child.component)
				is OfficersChild.Appointments -> AppointmentsListScreen(child.component)
			}
		}
	}

}
