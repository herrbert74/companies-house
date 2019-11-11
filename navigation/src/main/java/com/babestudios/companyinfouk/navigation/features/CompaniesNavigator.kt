package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.Navigator

interface CompaniesNavigator: Navigator {
    fun mainToCompany()
    fun mainToFavourites()
    fun mainToPrivacy()
    fun companyToMap()
    fun companyToCharges()
    fun companyToFilings()
    fun companyToInsolvencies()
    fun companyToOfficers()
    fun companyToPersons()
}