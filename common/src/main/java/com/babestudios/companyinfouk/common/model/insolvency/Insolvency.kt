package com.babestudios.companyinfouk.common.model.insolvency

import androidx.annotation.Keep

@Keep
data class Insolvency(val cases: List<InsolvencyCase> = emptyList())
