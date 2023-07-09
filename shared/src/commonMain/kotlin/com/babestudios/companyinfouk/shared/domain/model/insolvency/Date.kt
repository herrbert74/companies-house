package com.babestudios.companyinfouk.shared.domain.model.insolvency

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
data class Date (
	var date: String,
	var type: String,
): Parcelable
