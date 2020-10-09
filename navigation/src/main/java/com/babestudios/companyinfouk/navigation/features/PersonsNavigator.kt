package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.common.model.common.Address
import com.babestudios.companyinfouk.navigation.base.Navigator

interface PersonsNavigator: Navigator {
    fun personsToPersonDetails()
    fun personDetailsToMap(name: String, address: Address)
}
