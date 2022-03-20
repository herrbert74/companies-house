package com.babestudios.companyinfouk.domain.model.insolvency

import androidx.annotation.Keep

@Keep
data class Insolvency(val cases: List<InsolvencyCase> = emptyList())
