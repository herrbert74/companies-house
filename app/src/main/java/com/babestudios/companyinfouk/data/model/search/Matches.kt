package com.babestudios.companyinfouk.data.model.search

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Matches {

	@SerializedName("title")
	var title: List<Int> = ArrayList()

	@SerializedName("snippet")
	var snippet: List<Any> = ArrayList()

}
