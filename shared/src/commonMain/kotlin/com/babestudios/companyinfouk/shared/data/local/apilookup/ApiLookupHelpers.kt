package com.babestudios.companyinfouk.shared.data.local.apilookup

import com.babestudios.companyinfouk.shared.domain.ConstantMaps
import com.babestudios.companyinfouk.shared.domain.FilingHistoryDescriptionsMaps
import com.babestudios.companyinfouk.shared.domain.MortgageDescriptionMaps
import com.babestudios.companyinfouk.shared.domain.model.PscDescriptionMaps

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
