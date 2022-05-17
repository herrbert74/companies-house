package com.babestudios.companyinfouk.persons

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.babestudios.base.ext.navigateSafe
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_ADDRESS
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_NAME
import com.babestudios.companyinfouk.navigation.base.BaseNavigation
import com.babestudios.companyinfouk.navigation.features.PersonsBaseNavigatable
import com.babestudios.companyinfouk.persons.ui.persons.PersonsFragmentDirections

class PersonsNavigation : BaseNavigation(), PersonsBaseNavigatable {

	override var navController: NavController? = null

	override fun personsToPersonDetails(selectedPerson: Person) {
		val action = PersonsFragmentDirections.actionPersonsFragmentToPersonDetailsFragment(selectedPerson)
		navController?.navigateSafe(action)
	}

	override fun personDetailsToMap(name: String, address: Address) {
		val bundle = bundleOf(
			INDIVIDUAL_NAME to name,
			INDIVIDUAL_ADDRESS to address,
		)
		navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
	}

}
