package com.babestudios.companyinfouk.data.local.apilookup

import com.babestudios.companyinfouk.shared.domain.ConstantMaps
import com.babestudios.companyinfouk.shared.domain.FilingHistoryDescriptionsMaps
import com.babestudios.companyinfouk.shared.domain.MortgageDescriptionMaps
import com.babestudios.companyinfouk.shared.domain.model.PscDescriptionMaps

interface ConstantsHelperContract {
	fun accountTypeLookUp(accountsString: String): String
	fun sicLookUp(sicString: String): String
	fun insolvencyCaseType(caseTypeKey: String): String
	fun insolvencyCaseDateType(caseDateTypeKey: String): String
	fun officerRoleLookup(officerRoleKey: String): String
}

interface FilingHistoryDescriptionsHelperContract {
	fun filingHistoryLookUp(filingHistoryDescriptionString: String): String
}

interface ChargesHelperContract {
	fun statusLookUp(statusString: String): String
	fun filingTypeLookUp(filingTypeString: String): String
}

interface PscHelperContract {
	fun shortDescriptionLookUp(shortDescriptionString: String): String
	fun kindLookUp(kindString: String): String
}

object ConstantsHelper {

	fun accountTypeLookUp(accountsString: String) = ConstantMaps.accountType[accountsString] ?: accountsString

	fun sicLookUp(sicString: String) = ConstantMaps.sicDescriptions[sicString] ?: sicString

	fun insolvencyCaseType(caseTypeKey: String) = ConstantMaps.insolvencyCaseType[caseTypeKey] ?: caseTypeKey

	fun insolvencyCaseDateType(caseDateTypeKey: String) =
		ConstantMaps.insolvencyCaseDateType[caseDateTypeKey] ?: caseDateTypeKey

	fun officerRoleLookup(officerRoleKey: String) = ConstantMaps.officerRole[officerRoleKey] ?: officerRoleKey

}

object FilingHistoryDescriptionsHelper {

	fun filingHistoryLookUp(filingHistoryDescriptionString: String) =
		FilingHistoryDescriptionsMaps.description[filingHistoryDescriptionString] ?: filingHistoryDescriptionString

}

object ChargesHelper {

	fun statusLookUp(statusString: String) = MortgageDescriptionMaps.status[statusString] ?: statusString

	fun filingTypeLookUp(filingTypeString: String) =
		MortgageDescriptionMaps.filingType[filingTypeString] ?: filingTypeString

}

object PscHelper {

	fun shortDescriptionLookUp(shortDescriptionString: String) =
		PscDescriptionMaps.shortDescription[shortDescriptionString] ?: shortDescriptionString

	fun kindLookUp(kindString: String) = PscDescriptionMaps.kind[kindString] ?: kindString

}
