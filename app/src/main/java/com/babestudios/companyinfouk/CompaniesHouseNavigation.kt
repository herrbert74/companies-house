package com.babestudios.companyinfouk

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.babestudios.companyinfouk.navigation.COMPANY_NAME
import com.babestudios.companyinfouk.navigation.COMPANY_NUMBER
import com.babestudios.companyinfouk.navigation.base.BaseNavigator
import com.babestudios.companyinfouk.navigation.di.NavigationComponent
import com.babestudios.companyinfouk.navigation.features.*


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

			override fun mainToFavourites() {
				navController?.navigateSafe(R.id.action_mainFragment_to_favouritesFragment)
			}

			override fun mainToPrivacy() {
				navController?.navigateSafe(R.id.action_mainFragment_to_privacyFragment)
			}

			override fun companyToMap() {
				navController?.navigateSafe(R.id.action_companyFragment_to_mapFragment)
			}

			override fun companyToCharges() {
				navController?.navigateSafe(R.id.action_companyFragment_to_chargesActivity)
			}

			override fun companyToFilings() {
				navController?.navigateSafe(R.id.action_companyFragment_to_filingsActivity)
			}

			override fun companyToInsolvencies() {
				navController?.navigateSafe(R.id.action_companyFragment_to_insolvenciesActivity)
			}

			override fun companyToOfficers() {
				navController?.navigateSafe(R.id.action_companyFragment_to_officersActivity)
			}

			override fun companyToPersons() {
				navController?.navigateSafe(R.id.action_companyFragment_to_personsActivity)
			}
		}
	}

	override fun provideInsolvenciesNavigation(): InsolvenciesNavigator {
		return object : BaseNavigator(), InsolvenciesNavigator {
			override var navController: NavController? = null

			override fun insolvenciesToInsolvencyDetails() {
				navController?.navigateSafe(R.id.action_insolvenciesFragment_to_insolvencyDetailsFragment)
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

			override fun officersAppointmentsToCompanyActivity(companyNumber: String, companyName: String) {
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
