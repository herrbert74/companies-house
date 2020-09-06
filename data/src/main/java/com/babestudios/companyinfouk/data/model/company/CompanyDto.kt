package com.babestudios.companyinfouk.data.model.company

import android.os.Parcelable
import com.babestudios.companyinfouk.data.model.common.AddressDto
import java.util.ArrayList

import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CompanyDto(

		@SerializedName("accounts")
		var accounts: AccountsDto? = null,

		@SerializedName("annual_return")
		var annualReturn: AnnualReturnDto? = null,

		@SerializedName("branch_company_details")
		var branchCompanyDetails: BranchCompanyDetailsDto? = null,

		@SerializedName("can_file")
		var canFile: String? = null,

		@SerializedName("company_name")
		var companyName: String? = null,

		@SerializedName("company_number")
		var companyNumber: String? = null,

		@SerializedName("company_status")
		var companyStatus: String? = null,

		@SerializedName("company_status_detail")
		var companyStatusDetail: String? = null,

		@SerializedName("confirmation_statement")
		var confirmationStatement: ConfirmationStatementDto? = null,

		@SerializedName("date_of_cessation")
		var dateOfCessation: String? = null,

		@SerializedName("date_of_creation")
		var dateOfCreation: String? = null,

		@SerializedName("etag")
		var etag: String? = null,

		@SerializedName("foreign_company_details")
		var foreignCompanyDetails: ForeignCompanyDetailsDto? = null,

		@SerializedName("has_been_liquidated")
		var hasBeenLiquidated: String? = null,

		@SerializedName("has_charges")
		var hasCharges: Boolean = true,

		@SerializedName("has_insolvency_history")
		var hasInsolvencyHistory: Boolean = true,

		@SerializedName("is_community_interest_company")
		var isCommunityInterestCompany: String? = null,

		@SerializedName("jurisdiction")
		var jurisdiction: String? = null,

		@SerializedName("last_full_members_list_date")
		var lastFullMembersListDate: String? = null,

		@SerializedName("links")
		var links: CompanyLinksDto? = null,

		@SerializedName("partial_data_available")
		var partialDataAvailable: String? = null,

		@SerializedName("previous_company_names")
		var previousCompanyNames: List<PreviousCompanyNameDto> = ArrayList(),

		@SerializedName("registered_office_address")
		var registeredOfficeAddress: AddressDto? = null,

		@SerializedName("registered_office_is_in_dispute")
		var registeredOfficeIsInDispute: String? = null,

		@SerializedName("sic_codes")
		var sicCodes: List<String> = ArrayList(),

		@SerializedName("type")
		var type: String? = null,

		@SerializedName("undeliverable_registered_office_address")
		var undeliverableRegisteredOfficeAddress: String? = null
) : Parcelable
