package com.babestudios.companyinfouk.ui.officerdetails

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.data.model.officers.OfficerItem
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	OFFICER_ITEM_RECEIVED
}

@Parcelize
data class OfficerDetailsState(
		var officerItem: OfficerItem? = null,
		var officerId: String = "",
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable