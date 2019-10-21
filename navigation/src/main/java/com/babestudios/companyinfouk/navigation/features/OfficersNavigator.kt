package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.navigation.base.Navigator

interface OfficersNavigator: Navigator {
    fun officersToOfficerDetails()
    fun officersDetailsToAppointments(extras: androidx.navigation.Navigator.Extras)
}