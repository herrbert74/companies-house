package com.babestudios.companyinfouk.companies.ui.company

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.SingleLineCard
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.getAddressString
import com.babestudios.companyinfouk.domain.model.company.Company

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun CompanyScreen(component: CompanyComp) {

	val model by component.state.subscribeAsState()

	BackHandler(onBack = { component.onBackClicked(model.isFavourite) })

	val viewMarginLarge = dimensionResource(com.babestudios.base.R.dimen.viewMarginLarge)

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_company,
		navigationAction = { component.onBackClicked(model.isFavourite) },
		topAppBarActions = {},
		title = model.company.companyName
	) {

		CompanyScreenBody(
			model.company,
			component::onMapClicked,
			component::onChargesClicked,
			component::onFilingsClicked,
			component::onInsolvenciesClicked,
			component::onOfficersClicked,
			component::onPersonsClicked,
		)

	}

	Box(Modifier.fillMaxSize(1f)) {
		LargeFloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(all = viewMarginLarge)
				.testTag("Fab Favourite"),
			onClick = { component::onToggleFavouriteClicked.invoke() },
		) {
			Icon(
				painter = painterResource(
					if (model.isFavourite) R.drawable.ic_favorite_clear else R.drawable
						.ic_favorite
				),
				contentDescription = "Add favourites",
			)
		}
	}

}

@Composable
private fun CompanyScreenBody(
	company: Company,
	onMapClicked: (String) -> Unit,
	onChargesClicked: (String) -> Unit,
	onFilingsClicked: (String) -> Unit,
	onInsolvenciesClicked: (String) -> Unit,
	onOfficersClicked: (String) -> Unit,
	onPersonsClicked: (String) -> Unit,
) {

	val viewMarginLarge = dimensionResource(com.babestudios.base.R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(com.babestudios.base.R.dimen.viewMargin)

	Column(
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier
			.verticalScroll(rememberScrollState())
			.testTag("Company Screen Column")
	) {

		Text(
			text = company.companyNumber,
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			style = CompaniesTypography.titleLargeBold,
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
			title = stringResource(com.babestudios.companyinfouk.common.R.string.office_address),
			address = company.registeredOfficeAddress,
			onShowMap = { onMapClicked(company.registeredOfficeAddress.getAddressString()) },
		)
		Divider(thickness = 1.dp)
		TwoLineCard(
			stringResource(R.string.company_nature_of_business),
			company.natureOfBusiness,
			Modifier.fillMaxWidth(1f)
		)
		Divider(thickness = 1.dp)
		TwoLineCard(
			stringResource(R.string.company_accounts),
			company.lastAccountsMadeUpTo,
			Modifier.fillMaxWidth(1f)
		)
		Divider(thickness = 1.dp)
		SingleLineCard(
			modifier = Modifier
				.padding(vertical = viewMarginNormal)
				.clickable { onFilingsClicked(company.companyNumber) },
			vectorImageResource = R.drawable.ic_company_filing_history,
			text = stringResource(com.babestudios.companyinfouk.common.R.string.filing_history)
		)
		if (company.hasInsolvencyHistory) {
			SingleLineCard(
				modifier = Modifier
					.padding(vertical = viewMarginNormal)
					.clickable { onInsolvenciesClicked(company.companyNumber) },
				vectorImageResource = R.drawable.ic_company_insolvency,
				text = stringResource(com.babestudios.companyinfouk.common.R.string.insolvency)
			)
		}
		if (company.hasCharges) {
			SingleLineCard(
				modifier = Modifier
					.padding(vertical = viewMarginNormal)
					.clickable { onChargesClicked(company.companyNumber) },
				vectorImageResource = R.drawable.ic_company_charges,
				text = stringResource(com.babestudios.companyinfouk.common.R.string.charges)
			)
		}
		SingleLineCard(
			modifier = Modifier
				.padding(vertical = viewMarginNormal)
				.clickable { onOfficersClicked(company.companyNumber) },
			vectorImageResource = R.drawable.ic_company_officers,
			text = stringResource(com.babestudios.companyinfouk.common.R.string.officers)
		)
		SingleLineCard(
			modifier = Modifier
				.padding(vertical = viewMarginNormal)
				.clickable { onPersonsClicked(company.companyNumber) },
			vectorImageResource = R.drawable.ic_company_persons_with_control,
			text = stringResource(com.babestudios.companyinfouk.common.R.string.persons_with_significant_control)
		)
		Spacer(modifier = Modifier.height(viewMarginLarge))
	}

}

@Preview
@Composable
fun CompanyScreenPreview(@PreviewParameter(CompanyProvider::class) company: Company) {
	CompaniesTheme {
		CompanyScreenBody(company, {}, {}, {}, {}, {}) {}
	}
}

class CompanyProvider : PreviewParameterProvider<Company> {
	override val values = sequenceOf(
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
			lastAccountsMadeUpTo = "Last Full account"// made up to 31 July 2022"
		)
	)
}
