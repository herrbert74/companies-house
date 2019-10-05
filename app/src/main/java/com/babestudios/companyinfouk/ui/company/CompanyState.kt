package com.babestudios.companyinfouk.ui.company

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.data.model.company.Company
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	HIDE_FAB, //Hide fab by scaling, just to scale it back with the new state
	IS_FAVORITE,
	COMPANY_RECEIVED
}

@Parcelize
data class CompanyState(
		var company: Company?,
		var companyNumber: String = "",
		var companyName: String = "",
		var addressString: String = "",
		var natureOfBusinessString :String = "",
		var isFavorite :Boolean  = false,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable