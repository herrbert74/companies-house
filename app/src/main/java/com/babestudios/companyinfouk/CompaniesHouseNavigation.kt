package com.babestudios.companyinfouk

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_ADDRESS
import com.babestudios.companyinfouk.navigation.COMPANY_NAME
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.INDIVIDUAL_NAME
import com.babestudios.companyinfouk.navigation.base.BaseNavigator
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.ChargesNavigator
import com.babestudios.companyinfouk.navigation.features.CompaniesNavigator
import com.babestudios.companyinfouk.navigation.features.FilingsNavigator
import com.babestudios.companyinfouk.navigation.features.InsolvenciesNavigator
import com.babestudios.companyinfouk.navigation.features.OfficersNavigator
import com.babestudios.companyinfouk.navigation.features.PersonsNavigator

/**
 * This class holds the navController for any feature through [BaseNavigator]
 * It is a dependency of [com.babestudios.companyinfouk.core.injection.CoreComponent],
 * so no need to create it in every feature component.
 *
 * This holds all the implementations for [NavigationComponent],
 * and the feature navigations need to be exposed from the component,
 * like e.g. [com.babestudios.companyinfouk.officers.ui.OfficersComponent.navigator].
 */
internal class CompaniesHouseNavigation : NavigationComponent {

	//region features

	override fun provideFilingsNavigation(): FilingsNavigator {
		return object : BaseNavigator(), FilingsNavigator {
			override var navController: NavController? = null

			override fun filingsToFilingsDetails() {
				navController?.navigateSafe(R.id.action_filingsFragment_to_filingDetailsFragment)
			}
		}
	}

	override fun provideCompaniesNavigation(): CompaniesNavigator {
		return object : BaseNavigator(), CompaniesNavigator {
			override var navController: NavController? = null

			override fun mainToCompany() {
				navController?.navigateSafe(R.id.action_mainFragment_to_companyFragment)
			}

			override fun mainToCompanyPopMain() {
				navController?.navigateSafe(R.id.action_mainFragment_to_companyFragment_pop)
			}

			override fun mainToMapPopMain() {
				navController?.navigateSafe(R.id.action_mainFragment_to_mapFragment_pop)
			}

			override fun mainToFavourites() {
				navController?.navigateSafe(R.id.action_mainFragment_to_favouritesFragment)
			}

			override fun mainToPrivacy() {
				navController?.navigateSafe(R.id.action_mainFragment_to_privacyFragment)
			}

			override fun favouritesToCompany(companyNumber: String, companyName: String) {
				navController?.navigateSafe(R.id.action_favouritesFragment_to_companyFragment)
			}

			override fun companyToMap() {
				navController?.navigateSafe(R.id.action_companyFragment_to_mapFragment)
			}

			override fun companyToCharges(companyNumber: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
				navController?.navigateSafe(R.id.action_companyFragment_to_chargesActivity, bundle)
			}

			override fun companyToFilings(companyNumber: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
				navController?.navigateSafe(R.id.action_companyFragment_to_filingsActivity, bundle)
			}

			override fun companyToInsolvencies(companyNumber: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
				navController?.navigateSafe(R.id.action_companyFragment_to_insolvenciesActivity, bundle)
			}

			override fun companyToOfficers(companyNumber: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
				navController?.navigateSafe(R.id.action_companyFragment_to_officersActivity, bundle)
			}

			override fun companyToPersons(companyNumber: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber)
				navController?.navigateSafe(R.id.action_companyFragment_to_personsActivity, bundle)
			}
		}
	}

	override fun provideInsolvenciesNavigation(): InsolvenciesNavigator {
		return object : BaseNavigator(), InsolvenciesNavigator {
			override var navController: NavController? = null

			override fun insolvenciesToInsolvencyDetails() {
				navController?.navigateSafe(R.id.action_insolvenciesFragment_to_insolvencyDetailsFragment)
			}

			override fun insolvencyDetailsToPractitioner() {
				navController?.navigateSafe(R.id.action_insolvencyDetailsFragment_to_practitionerFragment)
			}
		}
	}

	override fun provideOfficersNavigation(): OfficersNavigator {
		return object : BaseNavigator(), OfficersNavigator {
			override var navController: NavController? = null

			override fun officersToOfficerDetails() {
				navController?.navigateSafe(R.id.action_officersFragment_to_officerDetailsFragment)
			}

			override fun officersDetailsToAppointments(extras: Navigator.Extras) {
				navController?.navigateSafe(
						R.id.action_officerDetailsFragment_to_officerAppointmentFragment,
						null,
						null,
						extras
				)
			}

			override fun officersDetailsToMap(name: String, address: Address) {
				val bundle = bundleOf(
						INDIVIDUAL_NAME to name,
						INDIVIDUAL_ADDRESS to address,
				)
				navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
			}

			override fun officersAppointmentsToCompany(companyNumber: String, companyName: String) {
				val bundle = bundleOf(COMPANY_NUMBER to companyNumber,
						COMPANY_NAME to companyName)
				navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
			}

		}
	}

	override fun providePersonsNavigation(): PersonsNavigator {
		return object : BaseNavigator(), PersonsNavigator {
			override var navController: NavController? = null

			override fun personsToPersonDetails() {
				navController?.navigateSafe(R.id.action_personsFragment_to_personDetailsFragment)
			}

			override fun personDetailsToMap(name: String, address: Address) {
				val bundle = bundleOf(
						INDIVIDUAL_NAME to name,
						INDIVIDUAL_ADDRESS to address,
				)
				navController?.navigateSafe(R.id.action_global_companyActivity, bundle)
			}

		}
	}

	override fun provideChargesNavigation(): ChargesNavigator {
		return object : BaseNavigator(), ChargesNavigator {
			override var navController: NavController? = null

			override fun chargesToChargesDetails() {
				navController?.navigateSafe(R.id.action_chargesFragment_to_chargesDetailsFragment)
			}

		}
	}

	//endregion


}

@Suppress("MaxLineLength")
		/**
		 * https://stackoverflow.com/questions/51060762/java-lang-illegalargumentexception-navigation-destination-xxx-is-unknown-to-thi
		 */
fun NavController.navigateSafe(
		@IdRes resId: Int,
		args: Bundle? = null,
		navOptions: NavOptions? = null,
		navExtras: Navigator.Extras? = null
) {
	val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
	if (action != null && currentDestination?.id != action.destinationId) {
		navigate(resId, args, navOptions, navExtras)
	}
}
