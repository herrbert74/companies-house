package com.babestudios.companyinfouk.ui.persons

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.ui.persons.list.AbstractPersonsVisitable
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	PERSONS_RECEIVED
}

@Parcelize
data class PersonsState(
		var persons: List<AbstractPersonsVisitable>?,
		var totalCount: Int? = null,
		var companyNumber: String? = "",
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable