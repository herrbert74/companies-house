package com.babestudios.companyinfouk.domain.model.charges

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Charges(
	var items: List<ChargesItem> = emptyList(),
	var totalCount: Int = 0
) : Parcelable
