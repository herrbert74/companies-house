package com.babestudios.companyinfouk

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.domain.model.persons.Person
import kotlinx.parcelize.Parcelize

sealed class Configuration : Parcelable {

	//region Companies

	@Parcelize
	object Main : Configuration()

	@Parcelize
	data class Company(
		val companyId: String,
		val companyName: String,
		val previousConfig: Configuration,
		val salt: Long, //This needs to be added to keep the Configuration unique, which is a Decompose requirement
	) : Configuration()

	@Parcelize
	object Favourites : Configuration()

	@Parcelize
	data class Map(val name: String, val address: String) : Configuration()

	@Parcelize
	object Privacy : Configuration()

	//endregion

	//region Charges

	@Parcelize
	data class Charges(val companyId: String) : Configuration()

	@Parcelize
	data class ChargesDetails(val selectedCharges: ChargesItem) : Configuration()

	//endregion

	//region Filings

	@Parcelize
	data class FilingHistory(val companyId: String) : Configuration()

	@Parcelize
	data class FilingDetails(val filingHistoryItem: FilingHistoryItem) : Configuration()

	//endregion

	//region Insolvencies

	@Parcelize
	data class Insolvencies(val companyId: String) : Configuration()

	@Parcelize
	data class InsolvencyDetails(
		val selectedCompanyId: String,
		val selectedInsolvencyCase: InsolvencyCase,
	) : Configuration()

	@Parcelize
	data class Practitioners(val selectedPractitioner: Practitioner) : Configuration()

	//endregion

	//region Officers

	@Parcelize
	data class Officers(val companyId: String) : Configuration()

	@Parcelize
	data class OfficerDetails(val selectedOfficer: Officer) : Configuration()

	@Parcelize
	data class Appointments(val selectedOfficer: Officer) : Configuration()

	//endregion

	//region Persons

	@Parcelize
	data class Persons(val companyId: String) : Configuration()

	@Parcelize
	data class PersonDetails(val selectedPerson: Person) : Configuration()

	//endregion

}
