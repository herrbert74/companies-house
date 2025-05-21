package com.babestudios.companyinfouk.shared.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.babestudios.companyinfouk.shared.screen.charges.ChargesComp
import com.babestudios.companyinfouk.shared.screen.chargedetails.ChargeDetailsComp
import com.babestudios.companyinfouk.shared.screen.company.CompanyComp
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesComp
import com.babestudios.companyinfouk.shared.screen.main.MainComp
import com.babestudios.companyinfouk.shared.screen.main.MainExecutor
import com.babestudios.companyinfouk.shared.screen.map.MapComp
import com.babestudios.companyinfouk.shared.screen.privacy.PrivacyComp
import com.babestudios.companyinfouk.shared.domain.api.CompaniesRepository
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.screen.filingdetails.FilingDetailsComp
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComp
import com.babestudios.companyinfouk.shared.screen.insolvencydetails.InsolvencyDetailsComp
import com.babestudios.companyinfouk.shared.screen.insolvencies.InsolvenciesComp
import com.babestudios.companyinfouk.shared.screen.practitionerdetails.PractitionerDetailsComp
import com.babestudios.companyinfouk.shared.screen.officerappointments.AppointmentsComp
import com.babestudios.companyinfouk.shared.screen.officerdetails.OfficerDetailsComp
import com.babestudios.companyinfouk.shared.screen.officers.OfficersComp
import com.babestudios.companyinfouk.shared.screen.persondetails.PersonDetailsComp
import com.babestudios.companyinfouk.shared.screen.persons.PersonsComp
import com.babestudios.companyinfouk.shared.domain.api.CompaniesDocumentRepository
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

interface CompaniesRootComp {
	val childStackValue: Value<ChildStack<Configuration, CompaniesChild>>
}

@Suppress("LongParameterList", "TooManyFunctions")
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
		mainContext: CoroutineDispatcher,
		ioContext: CoroutineDispatcher,
		companiesRepository: CompaniesRepository,
		companiesDocumentRepository: CompaniesDocumentRepository,
		finishHandler: () -> Unit,
	) : this(
		componentContext = componentContext,
		finishHandler,
		createCompanyComp = createCompanyFactory(companiesRepository, mainContext, ioContext),
		createFavouritesComp = createFavouritesFactory(companiesRepository, mainContext, ioContext),
		createMainComp = createMainFactory(MainExecutor(companiesRepository, mainContext, ioContext)),
		createMapComp = createMapFactory(mainContext),
		createPrivacyComp = createPrivacyFactory(mainContext),
		createChargesComp = createChargesFactory(companiesRepository, mainContext, ioContext),
		createChargeDetailsComp = createChargeDetailsFactory(mainContext),
		createFilingHistoryComp = createFilingsHistoryFactory(companiesRepository, mainContext, ioContext),
		createFilingDetailsComp = createFilingDetailsFactory(companiesDocumentRepository, mainContext, ioContext),
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
		saveStack = { null },
		restoreStack = { null },
		childFactory = ::createChild
	)

	override val childStackValue = stack

	@Suppress("CyclomaticComplexMethod")
	private fun createChild(configuration: Configuration, componentContext: ComponentContext): CompaniesChild {
		return when (configuration) {
			is Configuration.Main -> createMainChild(componentContext)
			is Configuration.Company -> createCompanyChild(componentContext, configuration)
			is Configuration.Favourites -> createFavouritesChild(componentContext)
			is Configuration.Map -> createMapChild(componentContext, configuration)
			is Configuration.Privacy -> createPrivacyChild(componentContext)
			is Configuration.Charges -> createChargesChild(componentContext, configuration)
			is Configuration.ChargesDetails -> createChargesDetailsChild(componentContext, configuration)
			is Configuration.FilingHistory -> createFilingHistoryChild(componentContext, configuration)
			is Configuration.FilingDetails -> createFilingDetailsChild(componentContext, configuration)
			is Configuration.Insolvencies -> createInsolvenciesChild(componentContext, configuration)
			is Configuration.InsolvencyDetails -> createInsolvencyDetailsChild(componentContext, configuration)
			is Configuration.Practitioners -> createPractitionersChild(componentContext, configuration)
			is Configuration.Officers -> createOfficersChild(componentContext, configuration)
			is Configuration.OfficerDetails -> createOfficerDetailsChild(componentContext, configuration)
			is Configuration.Appointments -> createAppointmentsChild(componentContext, configuration)
			is Configuration.Persons -> createPersonsChild(componentContext, configuration)
			is Configuration.PersonDetails -> createPersonDetailsChild(componentContext, configuration)
		}
	}

	private fun createMainChild(componentContext: ComponentContext) = CompaniesChild.Main(
		createMainComp(componentContext, finishHandler, FlowCollector(::onMainOutput))
	)

	private fun createCompanyChild(
		componentContext: ComponentContext,
		configuration: Configuration.Company,
	) = CompaniesChild.Company(
		createCompanyComp(
			componentContext,
			configuration.companyName,
			configuration.companyId,
			configuration.previousConfig == Configuration.Favourites,
			FlowCollector(::onCompanyOutput),
		)
	)

	private fun createFavouritesChild(componentContext: ComponentContext) = CompaniesChild.Favourites(
		createFavouritesComp(componentContext, FlowCollector(::onFavouritesOutput))
	)

	private fun createMapChild(
		componentContext: ComponentContext,
		configuration: Configuration.Map,
	) = CompaniesChild.Map(
		createMapComp(componentContext, configuration.name, configuration.address, FlowCollector(::onMapOutput))
	)

	private fun createPrivacyChild(componentContext: ComponentContext) = CompaniesChild.Privacy(
		createPrivacyComp(componentContext, FlowCollector(::onPrivacyOutput))
	)

	private fun createChargesChild(
		componentContext: ComponentContext,
		configuration: Configuration.Charges,
	) = CompaniesChild.Charges(
		createChargesComp(componentContext, configuration.companyId, FlowCollector(::onChargesOutput))
	)

	private fun createChargesDetailsChild(
		componentContext: ComponentContext,
		configuration: Configuration.ChargesDetails,
	) = CompaniesChild.ChargesDetails(
		createChargeDetailsComp(
			componentContext,
			configuration.selectedCharges,
			FlowCollector(::onChargeDetailsOutput)
		)
	)

	private fun createFilingHistoryChild(
		componentContext: ComponentContext,
		configuration: Configuration.FilingHistory,
	) = CompaniesChild.FilingHistory(
		createFilingHistoryComp(
			componentContext,
			configuration.companyId,
			FlowCollector(::onFilingHistoryOutput)
		)
	)

	private fun createFilingDetailsChild(
		componentContext: ComponentContext,
		configuration: Configuration.FilingDetails,
	) = CompaniesChild.FilingDetails(
		createFilingDetailsComp(
			componentContext,
			configuration.filingHistoryItem,
			FlowCollector(::onFilingDetailsOutput)
		)
	)

	private fun createInsolvenciesChild(
		componentContext: ComponentContext,
		configuration: Configuration.Insolvencies,
	) = CompaniesChild.Insolvencies(
		createInsolvenciesComp(componentContext, configuration.companyId, FlowCollector(::onInsolvenciesOutput))
	)

	private fun createInsolvencyDetailsChild(
		componentContext: ComponentContext,
		configuration: Configuration.InsolvencyDetails,
	) = CompaniesChild.InsolvencyDetails(
		createInsolvencyDetailsComp(
			componentContext,
			configuration.selectedCompanyId,
			configuration.selectedInsolvencyCase,
			FlowCollector(::onInsolvencyDetailsOutput)
		)
	)

	private fun createPractitionersChild(
		componentContext: ComponentContext,
		configuration: Configuration.Practitioners,
	) = CompaniesChild.Practitioners(
		createPractitionerDetailsComp(
			componentContext, configuration.selectedPractitioner, FlowCollector(::onPractitionerDetailsOutput)
		)
	)

	private fun createOfficersChild(
		componentContext: ComponentContext,
		configuration: Configuration.Officers,
	) = CompaniesChild.Officers(
		createOfficersComp(componentContext, configuration.companyId, FlowCollector(::onOfficersOutput))
	)

	private fun createOfficerDetailsChild(
		componentContext: ComponentContext,
		configuration: Configuration.OfficerDetails,
	) = CompaniesChild.OfficerDetails(
		createOfficerDetailsComp(
			componentContext,
			configuration.selectedOfficer,
			FlowCollector(::onOfficerDetailsOutput)
		)
	)

	private fun createAppointmentsChild(
		componentContext: ComponentContext,
		configuration: Configuration.Appointments,
	) = CompaniesChild.Appointments(
		createAppointmentsComp(
			componentContext,
			configuration.selectedOfficer,
			FlowCollector(::onAppointmentsOutput)
		)
	)

	private fun createPersonsChild(
		componentContext: ComponentContext,
		configuration: Configuration.Persons,
	) = CompaniesChild.Persons(
		createPersonsComp(componentContext, configuration.companyId, FlowCollector(::onPersonsListOutput))
	)

	private fun createPersonDetailsChild(
		componentContext: ComponentContext,
		configuration: Configuration.PersonDetails,
	) = CompaniesChild.PersonDetails(
		createPersonDetailsComp(
			componentContext,
			configuration.selectedPerson,
			FlowCollector(::onPersonDetailsOutput)
		)
	)

}
