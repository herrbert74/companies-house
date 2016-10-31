
package com.babestudios.companieshouse.data.model.company;

import com.babestudios.companieshouse.data.model.search.Links;
import com.google.gson.annotations.SerializedName;

public class CompanyLinks extends Links {

	@SerializedName("charges")
	public String charges;

	@SerializedName("filing_history")
	public String filingHistory;

	@SerializedName("insolvency")
	public String insolvency;

	@SerializedName("officers")
	public String officers;

	@SerializedName("persons_with_significant_control")
	public String personsWithSignificantControl;

	@SerializedName("persons_with_significant_control_statements")
	public String personsWithSignificantControlStatements;

	@SerializedName("registers")
	public String registers;

}
