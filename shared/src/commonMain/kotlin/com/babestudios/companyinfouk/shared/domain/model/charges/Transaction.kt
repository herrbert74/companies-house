package com.babestudios.companyinfouk.shared.domain.model.charges

import com.babestudios.companyinfouk.shared.Parcelable
import com.babestudios.companyinfouk.shared.Parcelize

@Parcelize
data class Transaction(var deliveredOn: String = "", var filingType: String = "") : Parcelable
