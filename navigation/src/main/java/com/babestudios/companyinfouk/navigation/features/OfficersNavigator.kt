package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.navigation.base.Navigator

interface OfficersNavigator: Navigator {
    fun officersToOfficerDetails()
    fun officersDetailsToAppointments(extras: androidx.navigation.Navigator.Extras)
    fun officersDetailsToMap(name: String, address: Address)
    fun officersAppointmentsToCompany(companyNumber: String, companyName: String)
}
