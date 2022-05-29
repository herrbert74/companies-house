package com.babestudios.companyinfouk.navigation

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

fun NavController.deepLinkNavigateTo(
	deepLinkDestination: DeepLinkDestination,
	popUpTo: Boolean = false,
	popUpDestination: Int = graph.startDestinationId

) {
	val builder = NavOptions.Builder()

	if (popUpTo) {
		builder.setPopUpTo(popUpDestination, false)
	}

	navigate(
		buildDeepLink(deepLinkDestination),
		builder.build()
	)
}

private fun buildDeepLink(destination: DeepLinkDestination) =
	NavDeepLinkRequest.Builder
		.fromUri(destination.address.toUri())
		.build()
