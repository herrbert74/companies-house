package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.Navigator

interface PersonsNavigator: Navigator {
    fun officersToOfficerDetails()
    fun officersDetailsToAppointments(extras: androidx.navigation.Navigator.Extras)
    fun officersAppointmentsToCompanyActivity(companyNumber: String, companyName: String)
}