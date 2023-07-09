package com.babestudios.companyinfouk.shared.domain.model.filinghistory

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
data class FilingHistoryLinks(
		val documentMetadata: String = "",
		val self: String = "",
) : Parcelable
