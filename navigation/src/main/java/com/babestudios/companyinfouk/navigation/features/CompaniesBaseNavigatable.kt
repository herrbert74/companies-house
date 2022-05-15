package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.BaseNavigatable

interface CompaniesBaseNavigatable: BaseNavigatable {
    fun mainToCompany()
    fun mainToCompanyPopMain()
    fun mainToMapPopMain()
    fun mainToFavourites()
    fun mainToPrivacy()
    fun favouritesToCompany(companyNumber: String, companyName: String)
    fun companyToMap()
    fun companyToCharges(companyNumber: String)
    fun companyToFilings(companyNumber: String)
    fun companyToInsolvencies(companyNumber: String)
    fun companyToOfficers(companyNumber: String)
    fun companyToPersons(companyNumber: String)
}
