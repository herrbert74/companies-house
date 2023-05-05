package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import java.util.ArrayList
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class CompanyDto(

		@SerialName("accounts")
		var accounts: AccountsDto? = null,

		@SerialName("annual_return")
		var annualReturn: AnnualReturnDto? = null,

		@SerialName("branch_company_details")
		var branchCompanyDetails: BranchCompanyDetailsDto? = null,

		@SerialName("can_file")
		var canFile: Boolean? = null,

		@SerialName("company_name")
		var companyName: String? = null,

		@SerialName("company_number")
		var companyNumber: String? = null,

		@SerialName("company_status")
		var companyStatus: String? = null,

		@SerialName("company_status_detail")
		var companyStatusDetail: String? = null,

		@SerialName("confirmation_statement")
		var confirmationStatement: ConfirmationStatementDto? = null,

		@SerialName("date_of_cessation")
		var dateOfCessation: String? = null,

		@SerialName("date_of_creation")
		var dateOfCreation: String? = null,

		@SerialName("etag")
		var etag: String? = null,

		@SerialName("foreign_company_details")
		var foreignCompanyDetails: ForeignCompanyDetailsDto? = null,

		@SerialName("has_been_liquidated")
		var hasBeenLiquidated: Boolean? = null,

		@SerialName("has_charges")
		var hasCharges: Boolean = true,

		@SerialName("has_insolvency_history")
		var hasInsolvencyHistory: Boolean = true,

		@SerialName("is_community_interest_company")
		var isCommunityInterestCompany: String? = null,

		@SerialName("jurisdiction")
		var jurisdiction: String? = null,

		@SerialName("last_full_members_list_date")
		var lastFullMembersListDate: String? = null,

		@SerialName("links")
		var links: CompanyLinksDto? = null,

		@SerialName("partial_data_available")
		var partialDataAvailable: String? = null,

		@SerialName("previous_company_names")
		var previousCompanyNames: List<PreviousCompanyNameDto> = ArrayList(),

		@SerialName("registered_office_address")
		var registeredOfficeAddress: AddressDto? = null,

		@SerialName("registered_office_is_in_dispute")
		var registeredOfficeIsInDispute: Boolean? = null,

		@SerialName("sic_codes")
		var sicCodes: List<String> = ArrayList(),

		@SerialName("type")
		var type: String? = null,

		@SerialName("undeliverable_registered_office_address")
		var undeliverableRegisteredOfficeAddress: Boolean? = null
) : Parcelable
