package com.babestudios.companyinfouk.navigation

interface ToFlowNavigatable {
    fun navigateToFlow(flow: NavigationFlow)
    fun popBackStack()
}
