package com.babestudios.companyinfouk.domain.model.search

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.common.Address
import kotlinx.serialization.SerialName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
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
) : Parcelable

fun List<CompanySearchResultItem>.filterSearchResults(filterState: FilterState?): List<CompanySearchResultItem> {
	return this
		.filter { searchItem ->
			filterState == FilterState.FILTER_SHOW_ALL ||
				(searchItem.companyStatus != null
					&& searchItem.companyStatus.equals(filterState.toString(), ignoreCase = true))
		}
		.toList()
}
