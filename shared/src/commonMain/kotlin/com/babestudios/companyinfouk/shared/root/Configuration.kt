package com.babestudios.companyinfouk.shared.root

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.domain.model.persons.Person

sealed class Configuration : Parcelable {

	//region Companies

	@Parcelize
	data object Main : Configuration()

	@Parcelize
	internal data class Company(
		val companyId: String,
		val companyName: String,
		val previousConfig: Configuration,
		val salt: Long, //This needs to be added to keep the Configuration unique, which is a Decompose requirement
	) : Configuration()

	@Parcelize
	internal data object Favourites : Configuration()

	@Parcelize
	internal data class Map(val name: String, val address: String) : Configuration()

	@Parcelize
	internal data object Privacy : Configuration()

	//endregion

	//region Charges

	@Parcelize
	internal data class Charges(val companyId: String) : Configuration()

	@Parcelize
	internal data class ChargesDetails(val selectedCharges: ChargesItem) : Configuration()

	//endregion

	//region Filings

	@Parcelize
	internal data class FilingHistory(val companyId: String) : Configuration()

	@Parcelize
	internal data class FilingDetails(val filingHistoryItem: FilingHistoryItem) : Configuration()

	//endregion

	//region Insolvencies

	@Parcelize
	internal data class Insolvencies(val companyId: String) : Configuration()

	@Parcelize
	internal data class InsolvencyDetails(
		val selectedCompanyId: String,
		val selectedInsolvencyCase: InsolvencyCase,
	) : Configuration()

	@Parcelize
	internal data class Practitioners(val selectedPractitioner: Practitioner) : Configuration()

	//endregion

	//region Officers

	@Parcelize
	internal data class Officers(val companyId: String) : Configuration()

	@Parcelize
	internal data class OfficerDetails(val selectedOfficer: Officer) : Configuration()

	@Parcelize
	internal data class Appointments(val selectedOfficer: Officer) : Configuration()

	//endregion

	//region Persons

	@Parcelize
	internal data class Persons(val companyId: String) : Configuration()

	@Parcelize
	internal data class PersonDetails(val selectedPerson: Person) : Configuration()

	//endregion

}
