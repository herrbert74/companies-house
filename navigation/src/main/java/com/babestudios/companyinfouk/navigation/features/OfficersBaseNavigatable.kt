package com.babestudios.companyinfouk.navigation.features

import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.navigation.base.BaseNavigatable

interface OfficersBaseNavigatable: BaseNavigatable {
    fun officersToOfficerDetails(selectedOfficer: Officer)
    fun officersDetailsToAppointments(selectedOfficer: Officer)
    fun officersDetailsToMap(name: String, address: Address)
    fun officersAppointmentsToCompany(companyNumber: String, companyName: String)
}
