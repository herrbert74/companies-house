package com.babestudios.companyinfouk.data.model.charges;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ChargesItem {
	@SerializedName("acquired_on")
	public String acquiredOn;
	@SerializedName("assets_ceased_released")
	public String assetsCeasedReleased;
	@SerializedName("charge_code")
	public String chargeCode;
	@SerializedName("charge_number")
	public String chargeNumber;
	@SerializedName("classification")
	public Classification classification;
	@SerializedName("covering_instrument_date")
	public String coveringInstrumentDate;
	@SerializedName("created_on")
	public String createdOn;
	@SerializedName("delivered_on")
	public String deliveredOn;
	@SerializedName("etag")
	public String etag;
	@SerializedName("id")
	public String id;
	@SerializedName("insolvency_cases")
	public List<InsolvencyCase> insolvencyCases = new ArrayList<>();
	@SerializedName("links")
	public ChargesItemLinks links;
	@SerializedName("more_than_four_persons_entitled")
	public String moreThanFourPersonsEntitled;
	@SerializedName("particulars")
	public Particulars particulars;
	@SerializedName("persons_entitled")
	public List<PersonsEntitled> personsEntitled = new ArrayList<>();
	@SerializedName("resolved_on")
	public String resolvedOn;
	@SerializedName("satisfied_on")
	public String satisfiedOn;
	@SerializedName("scottish_alterations")
	public ScottishAlterations scottishAlterations;
	@SerializedName("secured_details")
	public SecuredDetails securedDetails;
	@SerializedName("status")
	public String status;
	@SerializedName("transactions")
	public List<Transaction> transactions = new ArrayList<>();
}
