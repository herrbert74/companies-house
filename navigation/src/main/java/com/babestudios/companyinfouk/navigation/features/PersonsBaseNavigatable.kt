package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.navigation.base.BaseNavigatable

interface PersonsBaseNavigatable: BaseNavigatable {
    fun personsToPersonDetails()
    fun personDetailsToMap(name: String, address: Address)
}
