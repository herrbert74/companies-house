package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.GlobalNavigator
import com.babestudios.companyinfouk.navigation.base.Navigator

interface OfficersNavigator: Navigator, GlobalNavigator {
    fun officersToOfficerDetails()
}