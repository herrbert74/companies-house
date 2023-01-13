package com.babestudios.companyinfouk.companies.ui.company

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.SingleLineCard
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.company.Company
import java.util.*

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun CompanyScreen(component: CompanyComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_company,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = model.company.companyName
	) {
		CompanyScreenBody(state, model.company)
	}

}

@Composable
private fun CompanyScreenBody(state: ScrollState, company: Company) {
	Column(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.verticalScroll(state)
	) {

		val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
		val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

		Text(
			text = company.companyNumber,
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			style = CompaniesTypography.titleMedium,
		)
		Divider(thickness = 1.dp)
		Text(
			text = String.format(stringResource(R.string.incorporated_on), company.dateOfCreation),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			style = CompaniesTypography.bodyMedium,
		)
		Divider(thickness = 1.dp)
		AddressCard(
			address = company.registeredOfficeAddress,
			onShowMap = {},
		)
		Divider(thickness = 1.dp)
		TwoLineCard(
			stringResource(R.string.company_nature_of_business),
			company.natureOfBusiness
		)
		Divider(thickness = 1.dp)
		TwoLineCard(
			stringResource(R.string.company_accounts),
			company.lastAccountsMadeUpTo
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			vectorImageResource = R.drawable.ic_company_filing_history,
			text = stringResource(R.string.filing_history)
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			vectorImageResource = R.drawable.ic_company_insolvency,
			text = stringResource(R.string.insolvency)
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			vectorImageResource = R.drawable.ic_company_charges,
			text = stringResource(R.string.charges)
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			vectorImageResource = R.drawable.ic_company_officers,
			text = stringResource(R.string.officers)
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			vectorImageResource = R.drawable.ic_company_persons_with_control,
			text = stringResource(R.string.persons_with_significant_control)
		)
	}
}

@Preview("company Preview")
@Composable
fun CompanyScreenPreview() {
	CompaniesTheme {
		CompanyScreenBody(
			ScrollState(0),
			Company(
				companyNumber = "0234567",
				companyName = "Pbf Hire",
				dateOfCreation = "2012-02-02",
				registeredOfficeAddress = Address(
					addressLine1 = "1 Harewood Street",
					addressLine2 = null,
					country = "England",
					locality = "Leeds",
					postalCode = "LS2 7AD",
					region = "West Yorkshire"
				),
				natureOfBusiness = "64209 Activities of other holding companies not elsewhere classified",
				lastAccountsMadeUpTo = "Last Full account made up to 31 July 2022"
			)
		)
	}
}
