package com.babestudios.companyinfouk.domain.model.insolvency


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Date (
	var date: String,
	var type: String,
): Parcelable
