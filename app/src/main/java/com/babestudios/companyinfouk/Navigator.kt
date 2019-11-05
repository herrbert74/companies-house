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
 * It is injected through [com.babestudios.companyinfouk.core.injection.CoreComponent],
 * so no need to create it in every feature component.
 *
 * When injected, it can be referenced as any feature Navigator,
 * because it contains all interfaces through [NavigationComponent],
 * but it needs to be exposed from the component,
 * like e.g. [com.babestudios.companyinfouk.officers.ui.OfficersComponent.navigator].
 */
internal class Navigator : BaseNavigator(),
		FilingsNavigator,
		OfficersNavigator,
		PersonsNavigator,
		ChargesNavigator,
		InsolvenciesNavigator,
		NavigationComponent {

	//region global

	override fun popBackStack() {
		navController?.popBackStack()
	}

	//endregion

	//region features

	override fun provideFilingsNavigation(): FilingsNavigator {
		return this
	}

	override fun provideInsolvenciesNavigation(): InsolvenciesNavigator {
		return this
	}

	override fun provideOfficersNavigation(): OfficersNavigator {
		return this
	}

	override fun providePersonsNavigation(): PersonsNavigator {
		return this
	}

	override fun provideChargesNavigation(): ChargesNavigator {
		return this
	}

	//endregion

	//region officers

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

	//endregion

	//region insolvencies

	override fun insolvenciesToInsolvencyDetails() {
		navController?.navigateSafe(R.id.action_insolvenciesFragment_to_insolvencyDetailsFragment)
	}

	//endregion

	//region persons

	override fun personsToPersonDetails() {
		navController?.navigateSafe(R.id.action_personsFragment_to_personDetailsFragment)
	}

	//endregion

	//region charges

	override fun chargesToChargesDetails() {
		navController?.navigateSafe(R.id.action_chargesFragment_to_chargesDetailsFragment)
	}

	//endregion

	//region filings

	override fun filingsToFilingsDetails() {
		navController?.navigateSafe(R.id.action_filingsFragment_to_filingDetailsFragment)
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
