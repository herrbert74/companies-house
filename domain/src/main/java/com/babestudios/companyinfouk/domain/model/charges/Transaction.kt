package com.babestudios.companyinfouk.domain.model.charges

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(var deliveredOn: String = "", var filingType: String = "") : Parcelable
