package com.babestudios.companyinfouk.main

import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.babestudios.companyinfouk.charges.ui.charges.ChargesComp
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsComp
import com.babestudios.companyinfouk.companies.ui.company.CompanyComp
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComp
import com.babestudios.companyinfouk.companies.ui.main.MainComp
import com.babestudios.companyinfouk.companies.ui.map.MapComp
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyComp
import com.babestudios.companyinfouk.filings.ui.details.FilingDetailsComp
import com.babestudios.companyinfouk.filings.ui.filings.FilingHistoryComp
import com.babestudios.companyinfouk.insolvencies.ui.details.InsolvencyDetailsComp
import com.babestudios.companyinfouk.insolvencies.ui.insolvencies.InsolvenciesComp
import com.babestudios.companyinfouk.insolvencies.ui.practitioner.PractitionerDetailsComp
import com.babestudios.companyinfouk.officers.ui.appointments.AppointmentsComp
import com.babestudios.companyinfouk.officers.ui.details.OfficerDetailsComp
import com.babestudios.companyinfouk.officers.ui.officers.OfficersComp
import com.babestudios.companyinfouk.persons.ui.details.PersonDetailsComp
import com.babestudios.companyinfouk.persons.ui.persons.PersonsComp
import kotlin.random.Random

internal val navigation = StackNavigation<Configuration>()

//region Companies

internal fun onMainOutput(output: MainComp.Output): Unit = when (output) {
		is MainComp.Output.Selected ->
			navigation.push(
				Configuration.Company(
					output.companySearchResultItem.companyNumber!!,
					output.companySearchResultItem.title!!,
					Configuration.Main,
					Random.nextLong()
				)
			)

		is MainComp.Output.Privacy -> navigation.push(Configuration.Privacy)

		is MainComp.Output.RecentSearchHistorySelected -> {
			navigation.push(
				Configuration.Company(
					output.searchHistoryItem.companyNumber,
					output.searchHistoryItem.companyName,
					Configuration.Main,
					Random.nextLong()
				)
			)
		}

		MainComp.Output.Favourites -> navigation.push(Configuration.Favourites)
	}

internal fun onCompanyOutput(output: CompanyComp.Output) = when (output) {
	is CompanyComp.Output.Map -> navigation.push(Configuration.Map(output.name, output.address))
	is CompanyComp.Output.Charges -> navigation.push(Configuration.Charges(output.selectedCompanyId))
	is CompanyComp.Output.Filings -> navigation.push(Configuration.FilingHistory(output.selectedCompanyId))
	is CompanyComp.Output.Insolvencies -> navigation.push(Configuration.Insolvencies(output.selectedCompanyId))
	is CompanyComp.Output.Officers -> navigation.push(Configuration.Officers(output.selectedCompanyId))
	is CompanyComp.Output.Persons -> navigation.push(Configuration.Persons(output.selectedCompanyId))

	is CompanyComp.Output.Back -> {
		navigation.pop {
			if (goingToFavouritesAndRemovedFavourite(output.isComingFromFavourites, output.isFavourite)) {
				navigation.pop()
				navigation.push(Configuration.Favourites) //replaceCurrent does not work here!
			}
		}
	}

}

private fun goingToFavouritesAndRemovedFavourite(isComingFromFavourites: Boolean, isFavourite: Boolean) =
	isComingFromFavourites && !isFavourite

internal fun onFavouritesOutput(output: FavouritesComp.Output) = when (output) {
	is FavouritesComp.Output.Selected ->
		navigation.push(
			Configuration.Company(
				output.favouritesItem.searchHistoryItem.companyNumber,
				output.favouritesItem.searchHistoryItem.companyName,
				Configuration.Favourites,
				Random.nextLong()
			)
		)

	FavouritesComp.Output.Back -> navigation.pop()
}

internal fun onMapOutput(output: MapComp.Output) = when (output) {
	MapComp.Output.Back -> navigation.pop()
}

internal fun onPrivacyOutput(output: PrivacyComp.Output) = when (output) {
	PrivacyComp.Output.Back -> navigation.pop()
}

//endregion

//region Charges

internal fun onChargesOutput(output: ChargesComp.Output) = when (output) {
	is ChargesComp.Output.Selected ->
		navigation.push(Configuration.ChargesDetails(selectedCharges = output.chargesItem))

	ChargesComp.Output.Back -> navigation.pop()
}

internal fun onChargeDetailsOutput(output: ChargeDetailsComp.Output) = when (output) {
	ChargeDetailsComp.Output.Back -> navigation.pop()
}

//endregion

//region Filings

internal fun onFilingHistoryOutput(output: FilingHistoryComp.Output) = when (output) {
	is FilingHistoryComp.Output.Selected ->
		navigation.push(Configuration.FilingDetails(filingHistoryItem = output.filingHistoryItem))

	FilingHistoryComp.Output.Back -> navigation.pop()

}

internal fun onFilingDetailsOutput(output: FilingDetailsComp.Output) = when (output) {
	FilingDetailsComp.Output.Back -> navigation.pop()
}

//endregion

//region Insolvencies

internal fun onInsolvenciesOutput(output: InsolvenciesComp.Output) {
	when (output) {
		is InsolvenciesComp.Output.OnInsolvencyCaseClicked ->
			navigation.push(
				Configuration.InsolvencyDetails(
					selectedCompanyId = output.selectedCompanyId,
					selectedInsolvencyCase = output.selectedInsolvencyCase
				)
			)

		InsolvenciesComp.Output.Back -> navigation.pop()
	}
}

internal fun onInsolvencyDetailsOutput(output: InsolvencyDetailsComp.Output): Unit =
	when (output) {
		InsolvencyDetailsComp.Output.Back -> navigation.pop()
		is InsolvencyDetailsComp.Output.Selected ->
			navigation.push(Configuration.Practitioners(selectedPractitioner = output.practitioner))
	}

internal fun onPractitionerDetailsOutput(output: PractitionerDetailsComp.Output): Unit =
	when (output) {
		PractitionerDetailsComp.Output.Back -> navigation.pop()
		is PractitionerDetailsComp.Output.OnShowMapClicked ->
			navigation.push(Configuration.Map(output.name, output.address))
	}

//endregion

//region Officers

internal fun onOfficersOutput(output: OfficersComp.Output) {
	when (output) {
		is OfficersComp.Output.Selected -> navigation.push(
			Configuration.OfficerDetails(selectedOfficer = output.officer)
		)

		OfficersComp.Output.Finish -> navigation.pop()
	}
}

internal fun onOfficerDetailsOutput(output: OfficerDetailsComp.Output): Unit =
	when (output) {
		is OfficerDetailsComp.Output.Back -> navigation.pop()

		is OfficerDetailsComp.Output.OnShowMapClicked ->
			navigation.push(Configuration.Map(output.name, output.address))

		is OfficerDetailsComp.Output.OnAppointmentsClicked -> navigation.push(
			Configuration.Appointments(selectedOfficer = output.selectedOfficer)
		)

	}

internal fun onAppointmentsOutput(output: AppointmentsComp.Output): Unit =
	when (output) {
		AppointmentsComp.Output.Back -> navigation.pop()
		is AppointmentsComp.Output.Selected -> navigation.push(
			Configuration.Company(
				output.appointment.appointedTo.companyNumber,
				output.appointment.appointedTo.companyName,
				Configuration.Main, //Only Favourites matter
				Random.nextLong()
			)
		)
	}

//endregion

//region Persons

internal fun onPersonsListOutput(output: PersonsComp.Output) {
	when (output) {
		is PersonsComp.Output.Selected -> navigation.push(Configuration.PersonDetails(selectedPerson = output.person))
		PersonsComp.Output.Finish -> navigation.pop()
	}
}

internal fun onPersonDetailsOutput(output: PersonDetailsComp.Output): Unit =
	when (output) {
		is PersonDetailsComp.Output.Back -> navigation.pop()
		is PersonDetailsComp.Output.OnShowMapClicked ->
			navigation.push(Configuration.Map(output.name, output.address))
	}

//endregion
