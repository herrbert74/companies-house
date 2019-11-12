package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.Navigator

interface CompaniesNavigator: Navigator {
    fun mainToCompany()
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