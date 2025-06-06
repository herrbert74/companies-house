@file:Suppress("TooManyFunctions")
package com.babestudios.companyinfouk.shared.root

import com.babestudios.companyinfouk.shared.screen.charges.ChargesComponent
import com.babestudios.companyinfouk.shared.screen.charges.ChargesExecutor
import com.babestudios.companyinfouk.shared.screen.chargedetails.ChargeDetailsComponent
import com.babestudios.companyinfouk.shared.screen.company.CompanyComponent
import com.babestudios.companyinfouk.shared.screen.company.CompanyExecutor
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesComponent
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesExecutor
import com.babestudios.companyinfouk.shared.screen.main.MainComponent
import com.babestudios.companyinfouk.shared.screen.main.MainExecutor
import com.babestudios.companyinfouk.shared.screen.map.MapComponent
import com.babestudios.companyinfouk.shared.screen.privacy.PrivacyComponent
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsComponent
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsExecutor
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComponent
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryExecutor
import com.babestudios.companyinfouk.shared.screen.insolvencydetails.InsolvencyDetailsComponent
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesComponent
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesExecutor
import com.babestudios.companyinfouk.shared.screen.practitionerdetails.PractitionerDetailsComponent
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsComponent
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsExecutor
import com.babestudios.companyinfouk.shared.screen.officerdetails.OfficerDetailsComponent
import com.babestudios.companyinfouk.shared.screen.officers.OfficersComponent
import com.babestudios.companyinfouk.shared.screen.officers.OfficersExecutor
import com.babestudios.companyinfouk.shared.screen.persondetails.PersonDetailsComponent
import com.babestudios.companyinfouk.shared.screen.persons.PersonsExecutor
import com.babestudios.companyinfouk.shared.screen.persons.PersonsComponent
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
import kotlinx.coroutines.CoroutineDispatcher

/**
 * These are higher order functions with common parameters used in the RootComponent,
 * that return functions, that create the Decompose feature components from feature specific parameters.
 */

//region Companies

internal fun createCompanyFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateCompanyComp = { childContext, companyName, companyId, isComingFromFavourites, output ->
	CompanyComponent(
		componentContext = childContext,
		companyExecutor = CompanyExecutor(companiesRepository, mainContext, ioContext),
		companyName = companyName,
		companyId = companyId,
		isComingFromFavourites = isComingFromFavourites,
		output = output,
	)
}

internal fun createFavouritesFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateFavouritesComp = { childContext, output ->
	FavouritesComponent(
		componentContext = childContext,
		favouritesExecutor = FavouritesExecutor(companiesRepository, mainContext, ioContext),
		output = output,
	)
}

internal fun createMainFactory(mainExecutor: MainExecutor): CreateMainComp = { childContext, finishHandler, output ->
	MainComponent(
		componentContext = childContext,
		mainExecutor = mainExecutor,
		finishHandler = finishHandler,
		output = output,
	)
}

internal fun createMapFactory(mainContext: CoroutineDispatcher): CreateMapComp =
	{ childContext, name, address, output ->
		MapComponent(
			componentContext = childContext,
			mainContext = mainContext,
			name = name,
			address = address,
			output = output,
		)
	}

internal fun createPrivacyFactory(mainContext: CoroutineDispatcher): CreatePrivacyComp = { childContext, output ->
	PrivacyComponent(
		componentContext = childContext,
		mainContext = mainContext,
		output = output,
	)
}

//endregion

//region Charges

internal fun createChargesFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateChargesComp = { childContext, companyId, output ->
	ChargesComponent(
		componentContext = childContext,
		chargesExecutor = ChargesExecutor(companiesRepository, mainContext, ioContext),
		companyNumber = companyId,
		output = output,
	)
}

internal fun createChargeDetailsFactory(
	mainContext: CoroutineDispatcher,
): CreateChargeDetailsComp = { childContext, chargesItem, output ->
	ChargeDetailsComponent(
		componentContext = childContext,
		selectedCharges = chargesItem,
		mainContext = mainContext,
		output = output,
	)
}

//endregion

//region Filings

internal fun createFilingsHistoryFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateFilingHistoryComp = { childContext, companyId, output ->
	FilingHistoryComponent(
		componentContext = childContext,
		selectedCompanyId = companyId,
		filingHistoryExecutor = FilingHistoryExecutor(companiesRepository, mainContext, ioContext),
		output = output,
	)
}

internal fun createFilingDetailsFactory(
	companiesDocumentRepository: CompaniesDocumentRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateFilingDetailsComp = { childContext, filingHistoryItem, output ->
	FilingDetailsComponent(
		componentContext = childContext,
		filingDetailsExecutor = FilingDetailsExecutor(companiesDocumentRepository, mainContext, ioContext),
		filingHistoryItem = filingHistoryItem,
		output = output,
	)
}

//endregion

//region Insolvencies

internal fun createInsolvenciesFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateInsolvenciesComp = { childContext, companyId, output ->
	InsolvenciesComponent(
		componentContext = childContext,
		selectedCompanyId = companyId,
		insolvenciesExecutor = InsolvenciesExecutor(companiesRepository, mainContext, ioContext),
		output = output,
	)
}

internal fun createInsolvencyDetailsFactory(mainContext: CoroutineDispatcher): CreateInsolvencyDetailsComp =
	{ childContext, companyId, insolvencyCase, output ->
		InsolvencyDetailsComponent(
			componentContext = childContext,
			mainContext = mainContext,
			selectedCompanyId = companyId,
			insolvencyCase = insolvencyCase,
			output = output,
		)
	}

internal fun createPractitionerDetailsFactory(mainContext: CoroutineDispatcher): CreatePractitionerDetailsComp =
	{ childContext, selectedPractitioner, output ->
		PractitionerDetailsComponent(
			componentContext = childContext,
			mainContext = mainContext,
			selectedPractitioner = selectedPractitioner,
			output = output,
		)
	}

//endregion

//region Officers

internal fun createOfficersFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateOfficersComp = { childContext, companyId, output ->
	OfficersComponent(
		componentContext = childContext,
		companyNumber = companyId,
		officersExecutor = OfficersExecutor(companiesRepository, mainContext, ioContext),
		output = output,
	)
}

internal fun createOfficerDetailsFactory(mainContext: CoroutineDispatcher): CreateOfficerDetailsComp =
	{ childContext, selectedOfficer, output ->
		OfficerDetailsComponent(
			componentContext = childContext,
			mainContext = mainContext,
			selectedOfficer = selectedOfficer,
			output = output,
		)
	}

internal fun createAppointmentsFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreateAppointmentsComp =
	{ childContext, selectedOfficer, output ->
		AppointmentsComponent(
			componentContext = childContext,
			appointmentsExecutor = AppointmentsExecutor(companiesRepository, mainContext, ioContext),
			selectedOfficer = selectedOfficer,
			output = output,
		)
	}

//endregion

//region Persons

internal fun createPersonsFactory(
	companiesRepository: CompaniesRepository,
	mainContext: CoroutineDispatcher,
	ioContext: CoroutineDispatcher,
): CreatePersonsComp = { childContext, companyId, output ->
	PersonsComponent(
		componentContext = childContext,
		companyNumber = companyId,
		personsExecutor = PersonsExecutor(companiesRepository, mainContext, ioContext),
		output = output,
	)
}

internal fun createPersonDetailsFactory(mainContext: CoroutineDispatcher): CreatePersonDetailsComp =
	{ childContext, selectedPerson, output ->
		PersonDetailsComponent(
			componentContext = childContext,
			mainContext = mainContext,
			selectedPerson = selectedPerson,
			output = output,
		)
	}

//endregion
