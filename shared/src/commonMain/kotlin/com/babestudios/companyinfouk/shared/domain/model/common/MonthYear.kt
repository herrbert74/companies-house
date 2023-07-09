package com.babestudios.companyinfouk.shared.domain.model.common

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
data class MonthYear(val year: Int?, val month: Int?) : Parcelable
