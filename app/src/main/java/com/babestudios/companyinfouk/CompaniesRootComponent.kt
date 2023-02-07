package com.babestudios.companyinfouk

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.babestudios.base.coroutines.MainDispatcher
import com.babestudios.companyinfouk.charges.ui.charges.ChargesComp
import com.babestudios.companyinfouk.charges.ui.details.ChargeDetailsComp
import com.babestudios.companyinfouk.companies.ui.company.CompanyComp
import com.babestudios.companyinfouk.companies.ui.favourites.FavouritesComp
import com.babestudios.companyinfouk.companies.ui.main.MainComp
import com.babestudios.companyinfouk.companies.ui.main.MainExecutor
import com.babestudios.companyinfouk.companies.ui.map.MapComp
import com.babestudios.companyinfouk.companies.ui.privacy.PrivacyComp
import com.babestudios.companyinfouk.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.model.persons.Person
import com.babestudios.companyinfouk.domain.util.IoDispatcher
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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.FlowCollector

typealias CreateCompanyComp
	= (ComponentContext, String, String, Boolean, FlowCollector<CompanyComp.Output>) -> CompanyComp

typealias CreateMainComp = (ComponentContext, () -> Unit, FlowCollector<MainComp.Output>) -> MainComp
typealias CreateFavouritesComp = (ComponentContext, FlowCollector<FavouritesComp.Output>) -> FavouritesComp

typealias CreateMapComp = (ComponentContext, String, String, FlowCollector<MapComp.Output>) -> MapComp
typealias CreatePrivacyComp = (ComponentContext, FlowCollector<PrivacyComp.Output>) -> PrivacyComp
typealias CreateChargesComp = (ComponentContext, String, FlowCollector<ChargesComp.Output>) -> ChargesComp
typealias CreateChargeDetailsComp
	= (ComponentContext, ChargesItem, FlowCollector<ChargeDetailsComp.Output>) -> ChargeDetailsComp

typealias CreateFilingHistoryComp
	= (ComponentContext, String, FlowCollector<FilingHistoryComp.Output>) -> FilingHistoryComp

typealias CreateFilingDetailsComp
	= (ComponentContext, FilingHistoryItem, FlowCollector<FilingDetailsComp.Output>) -> FilingDetailsComp

typealias CreateInsolvenciesComp
	= (ComponentContext, String, FlowCollector<InsolvenciesComp.Output>) -> InsolvenciesComp

typealias CreateInsolvencyDetailsComp
	= (ComponentContext, String, InsolvencyCase, FlowCollector<InsolvencyDetailsComp.Output>) -> InsolvencyDetailsComp

typealias CreatePractitionerDetailsComp
	= (ComponentContext, Practitioner, FlowCollector<PractitionerDetailsComp.Output>) -> PractitionerDetailsComp

typealias CreateOfficersComp
	= (ComponentContext, String, FlowCollector<OfficersComp.Output>) -> OfficersComp

typealias CreateOfficerDetailsComp
	= (ComponentContext, Officer, FlowCollector<OfficerDetailsComp.Output>) -> OfficerDetailsComp

typealias CreateAppointmentsComp
	= (ComponentContext, Officer, FlowCollector<AppointmentsComp.Output>) -> AppointmentsComp

typealias CreatePersonsComp
	= (ComponentContext, String, FlowCollector<PersonsComp.Output>) -> PersonsComp

typealias CreatePersonDetailsComp
	= (ComponentContext, Person, FlowCollector<PersonDetailsComp.Output>) -> PersonDetailsComp

internal interface CompaniesRootComp {
	val childStackValue: Value<ChildStack<Configuration, CompaniesChild>>
}

class CompaniesRootComponent internal constructor(
	componentContext: ComponentContext,
	private val finishHandler: () -> Unit,
	private val createCompanyComp: CreateCompanyComp,
	private val createFavouritesComp: CreateFavouritesComp,
	private val createMainComp: CreateMainComp,
	private val createMapComp: CreateMapComp,
	private val createPrivacyComp: CreatePrivacyComp,
	private val createChargesComp: CreateChargesComp,
	private val createChargeDetailsComp: CreateChargeDetailsComp,
	private val createFilingHistoryComp: CreateFilingHistoryComp,
	private val createFilingDetailsComp: CreateFilingDetailsComp,
	private val createInsolvenciesComp: CreateInsolvenciesComp,
	private val createInsolvencyDetailsComp: CreateInsolvencyDetailsComp,
	private val createPractitionerDetailsComp: CreatePractitionerDetailsComp,
	private val createOfficersComp: CreateOfficersComp,
	private val createOfficerDetailsComp: CreateOfficerDetailsComp,
	private val createAppointmentsComp: CreateAppointmentsComp,
	private val createPersonsComp: CreatePersonsComp,
	private val createPersonDetailsComp: CreatePersonDetailsComp,
) : CompaniesRootComp,
	ComponentContext by componentContext {

	constructor(
		componentContext: ComponentContext,
		@MainDispatcher mainContext: CoroutineDispatcher,
		@IoDispatcher ioContext: CoroutineDispatcher,
		companiesRepository: CompaniesRepository,
		finishHandler: () -> Unit,
		mainExecutor: MainExecutor,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createCompanyComp = createCompanyFactory(companiesRepository, mainContext, ioContext),
		createFavouritesComp = createFavouritesFactory(companiesRepository, mainContext, ioContext),
		createMainComp = createMainFactory(mainExecutor),
		createMapComp = createMapFactory(mainContext),
		createPrivacyComp = createPrivacyFactory(mainContext),
		createChargesComp = createChargesFactory(companiesRepository, mainContext, ioContext),
		createChargeDetailsComp = createChargeDetailsFactory(mainContext),
		createFilingHistoryComp = createFilingsHistoryFactory(companiesRepository, mainContext, ioContext),
		createFilingDetailsComp = createFilingDetailsFactory(companiesRepository, mainContext, ioContext),
		createInsolvenciesComp = createInsolvenciesFactory(companiesRepository, mainContext, ioContext),
		createInsolvencyDetailsComp = createInsolvencyDetailsFactory(mainContext),
		createPractitionerDetailsComp = createPractitionerDetailsFactory(mainContext),
		createOfficersComp = createOfficersFactory(companiesRepository, mainContext, ioContext),
		createOfficerDetailsComp = createOfficerDetailsFactory(mainContext),
		createAppointmentsComp = createAppointmentsFactory(companiesRepository, mainContext, ioContext),
		createPersonsComp = createPersonsFactory(companiesRepository, mainContext, ioContext),
		createPersonDetailsComp = createPersonDetailsFactory(mainContext),
	)

	private val stack = childStack(
		source = navigation,
		initialStack = { listOf(Configuration.Main) },
		handleBackButton = true,
		childFactory = ::createChild
	)

	override val childStackValue = stack

	private fun createChild(configuration: Configuration, componentContext: ComponentContext): CompaniesChild =
		when (configuration) {
			is Configuration.Main -> CompaniesChild.Main(
				createMainComp(componentContext, finishHandler, FlowCollector(::onMainOutput))
			)

			is Configuration.Company -> CompaniesChild.Company(
				createCompanyComp(
					componentContext,
					configuration.companyName,
					configuration.companyId,
					configuration == Configuration.Favourites,
					FlowCollector(::onCompanyOutput),
				)
			)

			is Configuration.Favourites -> CompaniesChild.Favourites(
				createFavouritesComp(componentContext, FlowCollector(::onFavouritesOutput))
			)

			is Configuration.Map -> CompaniesChild.Map(
				createMapComp(componentContext, configuration.name, configuration.address, FlowCollector(::onMapOutput))
			)

			is Configuration.Privacy -> CompaniesChild.Privacy(
				createPrivacyComp(componentContext, FlowCollector(::onPrivacyOutput))
			)

			is Configuration.Charges -> CompaniesChild.Charges(
				createChargesComp(componentContext, configuration.companyId, FlowCollector(::onChargesOutput))
			)

			is Configuration.ChargesDetails -> CompaniesChild.ChargesDetails(
				createChargeDetailsComp(
					componentContext,
					configuration.selectedCharges,
					FlowCollector(::onChargeDetailsOutput)
				)
			)

			is Configuration.FilingHistory -> CompaniesChild.FilingHistory(
				createFilingHistoryComp(
					componentContext,
					configuration.companyId,
					FlowCollector(::onFilingHistoryOutput)
				)
			)

			is Configuration.FilingDetails -> CompaniesChild.FilingDetails(
				createFilingDetailsComp(
					componentContext,
					configuration.filingHistoryItem,
					FlowCollector(::onFilingDetailsOutput)
				)
			)

			is Configuration.Insolvencies -> CompaniesChild.Insolvencies(
				createInsolvenciesComp(componentContext, configuration.companyId, FlowCollector(::onInsolvenciesOutput))
			)

			is Configuration.InsolvencyDetails -> CompaniesChild.InsolvencyDetails(
				createInsolvencyDetailsComp(
					componentContext,
					configuration.selectedCompanyId,
					configuration.selectedInsolvencyCase,
					FlowCollector(::onInsolvencyDetailsOutput)
				)
			)

			is Configuration.Practitioners -> CompaniesChild.Practitioners(
				createPractitionerDetailsComp(
					componentContext, configuration.selectedPractitioner, FlowCollector(::onPractitionerDetailsOutput)
				)
			)

			is Configuration.Officers -> CompaniesChild.Officers(
				createOfficersComp(componentContext, configuration.companyId, FlowCollector(::onOfficersOutput))
			)

			is Configuration.OfficerDetails -> CompaniesChild.OfficerDetails(
				createOfficerDetailsComp(
					componentContext,
					configuration.selectedOfficer,
					FlowCollector(::onOfficerDetailsOutput)
				)
			)

			is Configuration.Appointments -> CompaniesChild.Appointments(
				createAppointmentsComp(
					componentContext,
					configuration.selectedOfficer,
					FlowCollector(::onAppointmentsOutput)
				)
			)

			is Configuration.Persons -> CompaniesChild.Persons(
				createPersonsComp(componentContext, configuration.companyId, FlowCollector(::onPersonsListOutput))
			)

			is Configuration.PersonDetails -> CompaniesChild.PersonDetails(
				createPersonDetailsComp(
					componentContext,
					configuration.selectedPerson,
					FlowCollector(::onPersonDetailsOutput)
				)
			)
		}

}
