package com.babestudios.companyinfouk.navigation

sealed class NavigationFlow {
    class ChargesFlow(val selectedCompanyId: String) : NavigationFlow()
    class FilingsFlow(val selectedCompanyId: String) : NavigationFlow()
    class InsolvenciesFlow(val selectedCompanyId: String) : NavigationFlow()
    class OfficersFlow(val selectedCompanyId: String) : NavigationFlow()
    class PersonsFlow(val selectedCompanyId: String) : NavigationFlow()
}
