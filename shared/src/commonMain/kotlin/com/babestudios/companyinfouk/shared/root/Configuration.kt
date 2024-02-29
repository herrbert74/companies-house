package com.babestudios.companyinfouk.shared.root

import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import kotlinx.serialization.Serializable

@Serializable
sealed class Configuration {

	//region Companies

	@Serializable
	data object Main : Configuration()

	@Serializable
	internal data class Company(
		val companyId: String,
		val companyName: String,
		val previousConfig: Configuration,
		val salt: Long, //This needs to be added to keep the Configuration unique, which is a Decompose requirement
	) : Configuration()

	@Serializable
	internal data object Favourites : Configuration()

	@Serializable
	internal data class Map(val name: String, val address: String) : Configuration()

	@Serializable
	internal data object Privacy : Configuration()

	//endregion

	//region Charges

	@Serializable
	internal data class Charges(val companyId: String) : Configuration()

	@Serializable
	internal data class ChargesDetails(val selectedCharges: ChargesItem) : Configuration()

	//endregion

	//region Filings

	@Serializable
	internal data class FilingHistory(val companyId: String) : Configuration()

	@Serializable
	internal data class FilingDetails(val filingHistoryItem: FilingHistoryItem) : Configuration()

	//endregion

	//region Insolvencies

	@Serializable
	internal data class Insolvencies(val companyId: String) : Configuration()

	@Serializable
	internal data class InsolvencyDetails(
		val selectedCompanyId: String,
		val selectedInsolvencyCase: InsolvencyCase,
	) : Configuration()

	@Serializable
	internal data class Practitioners(val selectedPractitioner: Practitioner) : Configuration()

	//endregion

	//region Officers

	@Serializable
	internal data class Officers(val companyId: String) : Configuration()

	@Serializable
	internal data class OfficerDetails(val selectedOfficer: Officer) : Configuration()

	@Serializable
	internal data class Appointments(val selectedOfficer: Officer) : Configuration()

	//endregion

	//region Persons

	@Serializable
	internal data class Persons(val companyId: String) : Configuration()

	@Serializable
	internal data class PersonDetails(val selectedPerson: Person) : Configuration()

	//endregion

}
