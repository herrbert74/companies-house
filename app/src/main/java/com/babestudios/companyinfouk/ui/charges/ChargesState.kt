package com.babestudios.companyinfouk.ui.charges

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.charges.list.AbstractChargesVisitable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

enum class ContentChange {
	NONE,
	CHARGES_RECEIVED
}

@Parcelize
data class ChargesState(
		var chargeItems: @RawValue List<AbstractChargesVisitable>?,
		var totalCount: Int? = null,
		var companyNumber: String? = "",
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState() , Parcelable