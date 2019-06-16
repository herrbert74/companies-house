package com.babestudios.companyinfouk.data.model.officers.appointments


import com.babestudios.companyinfouk.data.model.common.MonthYear
import com.babestudios.companyinfouk.data.model.common.SelfLinkData
import com.google.gson.annotations.SerializedName

class Appointments {
	@SerializedName("date_of_birth")
	var dateOfBirth: MonthYear? = null
	@SerializedName("etag")
	var etag: String? = null
	@SerializedName("is_corporate_officer")
	var isCorporateOfficer: String? = null
	@SerializedName("items")
	var items: List<Appointment>? = null
	@SerializedName("items_per_page")
	var itemsPerPage: String? = null
	@SerializedName("kind")
	var kind: String? = null
	@SerializedName("links")
	var links: SelfLinkData? = null
	@SerializedName("name")
	var name: String? = null
	@SerializedName("start_index")
	var startIndex: String? = null
	@SerializedName("total_results")
	var totalResults: String? = null
}
