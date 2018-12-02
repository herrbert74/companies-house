package com.babestudios.companyinfouk.data.model.insolvency

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class InsolvencyCase {
	@SerializedName("dates")
	var dates: List<Date> = ArrayList()
	@SerializedName("links")
	var links: InsolvencyCaseLinks? = null
	@SerializedName("notes")
	var notes: List<String> = ArrayList()
	@SerializedName("number")
	var number: String? = null
	@SerializedName("practitioners")
	var practitioners: List<Practitioner> = ArrayList()
	@SerializedName("type")
	var type: String? = null
}
