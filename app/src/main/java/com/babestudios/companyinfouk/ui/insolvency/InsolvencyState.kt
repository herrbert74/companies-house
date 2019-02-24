package com.babestudios.companyinfouk.ui.insolvency

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.insolvency.list.InsolvencyVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	INSOLVENCIES_RECEIVED
}

@Parcelize
data class InsolvencyState(
		var insolvencyItems: List<InsolvencyVisitable>?,
		var companyNumber: String? = "",
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable