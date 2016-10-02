
package com.babestudios.companieshouse.data.model.company;

import com.babestudios.companieshouse.data.model.search.Links;
import com.google.gson.annotations.SerializedName;

public class CompanyLinks extends Links {

	@SerializedName("filing_history")
	public String filingHistory;

	@SerializedName("officers")
	public String officers;

}
