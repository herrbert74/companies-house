package com.babestudios.companyinfo.navigation.features

import com.babestudios.companyinfo.navigation.base.GlobalNavigator
import com.babestudios.companyinfo.navigation.base.Navigator

interface OfficersNavigator: Navigator, GlobalNavigator {
    fun officersToOfficerDetails()
}