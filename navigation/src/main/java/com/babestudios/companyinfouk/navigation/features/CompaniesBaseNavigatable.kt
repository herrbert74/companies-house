package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.BaseNavigatable

interface CompaniesBaseNavigatable : BaseNavigatable {
	fun mainToCompany(number: String, name: String)
	fun mainToFavourites()
	fun mainToPrivacy()
}
