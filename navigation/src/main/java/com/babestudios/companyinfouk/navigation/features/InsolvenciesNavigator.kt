package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.Navigator

interface InsolvenciesNavigator: Navigator {
    fun insolvenciesToInsolvencyDetails()
    fun insolvencyDetailsToPractitioner()
}
