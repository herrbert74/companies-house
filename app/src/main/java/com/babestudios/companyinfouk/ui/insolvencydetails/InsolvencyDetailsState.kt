package com.babestudios.companyinfouk.ui.insolvencydetails

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfo.data.model.insolvency.InsolvencyCase
import com.babestudios.companyinfouk.ui.insolvencydetails.list.AbstractInsolvencyDetailsVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	INSOLVENCY_CASE_RECEIVED
}

@Parcelize
data class InsolvencyDetailsState(
		var insolvencyDetailsItems: List<AbstractInsolvencyDetailsVisitable>?,
		var insolvencyCase: InsolvencyCase? = null,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable