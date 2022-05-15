package com.babestudios.companyinfouk.officers

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.navigation.COMPANY_NAME
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_ADDRESS
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_NAME
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.OfficersBaseNavigatable
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsFragmentDirections
import com.babestudios.companyinfouk.officers.ui.officers.OfficersFragmentDirections

class OfficersNavigation : BaseNavigation(), OfficersBaseNavigatable {

	override var navController: NavController? = null

	override fun officersToOfficerDetails(selectedOfficer: Officer) {
		val action = OfficersFragmentDirections.actionOfficersFragmentToOfficerDetailsFragment(selectedOfficer)
		navController?.navigateSafe(action)
	}

	override fun officersDetailsToAppointments(selectedOfficer: Officer) {
		val action =
			OfficerDetailsFragmentDirections.actionOfficerDetailsFragmentToOfficerAppointmentFragment(selectedOfficer)
		navController?.navigateSafe(action)
	}

	override fun officersDetailsToMap(name: String, address: Address) {
		val bundle = bundleOf(
			INDIVIDUAL_NAME to name,
			INDIVIDUAL_ADDRESS to address,
		)
		navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
	}

	override fun officersAppointmentsToCompany(companyNumber: String, companyName: String) {
		val bundle = bundleOf(
			COMPANY_NUMBER to companyNumber,
			COMPANY_NAME to companyName
		)
		navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
	}

}
