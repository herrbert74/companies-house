package com.babestudios.companyinfouk.ui.officers

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.officers.list.AbstractOfficersVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	OFFICERS_RECEIVED
}

@Parcelize
data class OfficersState(
		var officerItems: List<AbstractOfficersVisitable>?,
		var totalCount: Int? = null,
		var companyNumber: String? = "",
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable