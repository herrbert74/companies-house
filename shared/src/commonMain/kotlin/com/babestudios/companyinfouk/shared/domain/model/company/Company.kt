package com.babestudios.companyinfouk.shared.domain.model.company

import com.babestudios.companyinfouk.shared.domain.model.common.Address

data class Company(
	val companyName: String = "",
	val lastAccountsMadeUpTo: String = "",
	val companyNumber: String = "",
	val dateOfCreation: String = "",
	val hasCharges: Boolean = false,
	val hasInsolvencyHistory: Boolean = false,
	val registeredOfficeAddress: Address = Address(),
	val natureOfBusiness: String = "",
)
