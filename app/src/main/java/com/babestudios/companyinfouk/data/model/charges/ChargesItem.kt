package com.babestudios.companyinfouk.data.model.charges


import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class ChargesItem {
	@SerializedName("acquired_on")
	var acquiredOn: String? = null
	@SerializedName("assets_ceased_released")
	var assetsCeasedReleased: String? = null
	@SerializedName("charge_code")
	var chargeCode: String? = null
	@SerializedName("charge_number")
	var chargeNumber: String? = null
	@SerializedName("classification")
	var classification: Classification? = null
	@SerializedName("covering_instrument_date")
	var coveringInstrumentDate: String? = null
	@SerializedName("created_on")
	var createdOn: String? = null
	@SerializedName("delivered_on")
	var deliveredOn: String? = null
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("id")
	var id: String? = null
	@SerializedName("insolvency_cases")
	var insolvencyCases: List<InsolvencyCase> = ArrayList()
	@SerializedName("links")
	var links: ChargesItemLinks? = null
	@SerializedName("more_than_four_persons_entitled")
	var moreThanFourPersonsEntitled: String? = null
	@SerializedName("particulars")
	var particulars: Particulars? = null
	@SerializedName("persons_entitled")
	var personsEntitled: List<PersonsEntitled> = ArrayList()
	@SerializedName("resolved_on")
	var resolvedOn: String? = null
	@SerializedName("satisfied_on")
	var satisfiedOn: String? = null
	@SerializedName("scottish_alterations")
	var scottishAlterations: ScottishAlterations? = null
	@SerializedName("secured_details")
	var securedDetails: SecuredDetails? = null
	@SerializedName("status")
	var status: String? = null
	@SerializedName("transactions")
	var transactions: List<Transaction> = ArrayList()
}
