package com.babestudios.companyinfouk.data.model.search

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import com.babestudios.companyinfouk.data.model.common.SelfLinkData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

import java.util.ArrayList

@Parcelize
class CompanySearchResultItem (

		@SerializedName("companyLinks")
	var links: SelfLinkData? = null,

		@SerializedName("description_identifier")
	var descriptionIdentifier: List<String> = ArrayList(),

		@SerializedName("date_of_creation")
	var dateOfCreation: String? = null,

		@SerializedName("snippet")
	var snippet: String? = null,

		@SerializedName("company_number")
	var companyNumber: String? = null,

		@SerializedName("title")
	var title: String? = null,

		@SerializedName("company_status")
	var companyStatus: String? = null,

		@SerializedName("matches")
	var matches: Matches? = null,

		@SerializedName("address")
	var address: AddressDto? = null,

		@SerializedName("description")
	var description: String? = null,

		@SerializedName("kind")
	var kind: String? = null,

		@SerializedName("company_type")
	var companyType: String? = null,

		@SerializedName("address_snippet")
	var addressSnippet: String? = null
): Parcelable
