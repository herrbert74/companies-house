package com.babestudios.companyinfouk.domain.model.company

import android.os.Parcelable
import com.babestudios.companyinfouk.domain.model.common.Address
import kotlinx.parcelize.Parcelize

@Parcelize
data class Company(
	val companyName: String = "",
	val lastAccountsMadeUpTo: String = "",
	val companyNumber: String = "",
	val dateOfCreation: String = "",
	val hasCharges: Boolean = false,
	val hasInsolvencyHistory: Boolean = false,
	val registeredOfficeAddress: Address = Address(),
	val natureOfBusiness: String = "",
) : Parcelable
