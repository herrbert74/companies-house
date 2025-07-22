package com.babestudios.companyinfouk.shared.domain.model.enumerations

@Suppress("ConstructorParameterNaming")
data class Constants(
	val identification_type: Map<String, String>,
	val jurisdiction: Map<String, String>,
	val company_summary: Map<String, String>,
	val company_type: Map<String, String>,
	val company_subtype: Map<String, String>,
	val company_birth_type: Map<String, String>,
	val proof_status: Map<String, String>,
	val company_status: Map<String, String>,
	val company_status_detail: Map<String, String>,
	val foreign_account_type: Map<String, String>,
	val terms_of_account_publication: Map<String, String>,
	val account_type: Map<String, String>,
	val officer_role: Map<String, String>,
	val filing_type: Map<String, String>,
	val filing_status: Map<String, String>,
	val insolvency_case_type: Map<String, String>,
	val insolvency_case_date_type: Map<String, String>,
	val sic_descriptions: Map<String, String>,
	val notes: Map<String, String>,
	val cessation_label_for_status: Map<String, String>,
	val register_locations: Map<String, String>,
	val register_types: Map<String, String>,
	val partial_data_available: Map<String, String>,
)
