package com.babestudios.companyinfouk.shared.domain.model.search

import com.babestudios.companyinfouk.shared.domain.model.common.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanySearchResultItem(

	@SerialName("description_identifier")
	var descriptionIdentifier: List<String?> = listOf(),

	@SerialName("date_of_creation")
	var dateOfCreation: String? = null,

	@SerialName("snippet")
	var snippet: String? = null,

	@SerialName("company_number")
	var companyNumber: String? = null,

	@SerialName("title")
	var title: String? = null,

	@SerialName("company_status")
	var companyStatus: String? = null,

	@SerialName("matches")
	var matches: Matches? = null,

	@SerialName("address")
	var address: Address? = null,

	@SerialName("description")
	var description: String? = null,

	@SerialName("kind")
	var kind: String? = null,

	@SerialName("company_type")
	var companyType: String? = null,

	@SerialName("address_snippet")
	var addressSnippet: String? = null,
)

fun List<CompanySearchResultItem>.filterSearchResults(filterState: FilterState?): List<CompanySearchResultItem> {
	return this
		.filter { searchItem ->
			filterState == FilterState.FILTER_SHOW_ALL ||
				(searchItem.companyStatus != null
					&& searchItem.companyStatus.equals(filterState.toString(), ignoreCase = true))
		}
		.toList()
}
