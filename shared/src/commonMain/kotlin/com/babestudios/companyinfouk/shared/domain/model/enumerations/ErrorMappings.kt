package com.babestudios.companyinfouk.shared.domain.model.enumerations

data class ErrorMappings(val errors: Errors)

data class Errors(val service: Map<String, String>)
