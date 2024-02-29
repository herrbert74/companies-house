package com.babestudios.companyinfouk.shared.domain.model.charges

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(var deliveredOn: String = "", var filingType: String = "")
