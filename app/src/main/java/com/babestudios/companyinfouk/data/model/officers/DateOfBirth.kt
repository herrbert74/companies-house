package com.babestudios.companyinfouk.data.model.officers


import com.google.gson.annotations.SerializedName

class DateOfBirth {
	@SerializedName("year")
	var year: Long? = null
	@SerializedName("month")
	var month: Long? = null
}
