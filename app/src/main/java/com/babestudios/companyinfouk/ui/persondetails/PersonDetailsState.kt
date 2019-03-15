package com.babestudios.companyinfouk.ui.persondetails

import android.os.Parcelable
import com.babestudios.base.mvp.BaseState
import com.babestudios.companyinfouk.data.model.persons.Person
import kotlinx.android.parcel.Parcelize

enum class ContentChange {
	NONE,
	PERSON_ITEM_RECEIVED
}

@Parcelize
data class PersonDetailsState(
		var person: Person? = null,
		var contentChange: ContentChange = ContentChange.NONE
) : BaseState(), Parcelable