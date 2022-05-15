package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.BaseNavigatable

interface InsolvenciesBaseNavigatable: BaseNavigatable {
    fun insolvenciesToInsolvencyDetails()
    fun insolvencyDetailsToPractitioner()
}
