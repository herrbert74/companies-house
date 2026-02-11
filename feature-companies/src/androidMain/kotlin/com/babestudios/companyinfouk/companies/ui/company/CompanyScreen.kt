package com.babestudios.companyinfouk.companies.ui.company

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.SingleLineCard
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.BodyMediumText
import com.babestudios.companyinfouk.design.component.TitleLargeBoldText
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.common.getAddressString
import com.babestudios.companyinfouk.shared.domain.model.company.Company
import com.babestudios.companyinfouk.shared.screen.company.CompanyComp

@Composable
@Suppress("LongMethod", "ComplexMethod", "Wrapping")
fun CompanyScreen(
	component: CompanyComp,
	modifier: Modifier = Modifier,
) {
	val model by component.state.subscribeAsState()

	val onBackLambda = remember(model.isFavourite) {
		{ component.onBackClicked(model.isFavourite) }
	}

	BackHandler(onBack = onBackLambda)

	val viewMarginLarge = Dimens.marginLarge

	CollapsingToolbarScaffold(
		modifier = modifier,
		backgroundDrawable = R.drawable.bg_company,
		title = model.company.companyName,
		onBackClick = { component.onBackClicked(model.isFavourite) },
	) { paddingValues ->
		CompanyScreenBody(
			model.company,
			paddingValues,
			component::onMapClicked,
			component::onChargesClicked,
			component::onFilingsClicked,
			component::onInsolvenciesClicked,
			component::onOfficersClicked,
			component::onPersonsClicked,
		)

		Box(Modifier.fillMaxSize(1f)) {
			val onToggleFavouriteClickedLambda = remember {
				{
					component.onToggleFavouriteClicked()
				}
			}
			LargeFloatingActionButton(
				modifier = Modifier
					.align(Alignment.BottomEnd)
					.padding(bottom = viewMarginLarge + paddingValues.calculateBottomPadding(), end = viewMarginLarge)
					.testTag("Fab Favourite"),
				onClick = onToggleFavouriteClickedLambda,
			) {
				Icon(
					painter = painterResource(
						if (model.isFavourite) R.drawable.ic_favorite_clear else R.drawable.ic_favorite
					),
					contentDescription = "Add favourites",
				)
			}
		}
	}
}

@Suppress("Wrapping", "LongMethod")
@Composable
private fun CompanyScreenBody(
	company: Company,
	paddingValues: PaddingValues,
	onMapClick: (String) -> Unit,
	onChargesClick: (String) -> Unit,
	onFilingsClick: (String) -> Unit,
	onInsolvenciesClick: (String) -> Unit,
	onOfficersClick: (String) -> Unit,
	onPersonsClick: (String) -> Unit,
) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	val incorporatedOnText = stringResource(R.string.incorporated_on)

	val formattedIncorporatedOnText = remember(company.dateOfCreation, incorporatedOnText) {
		String.format(incorporatedOnText, company.dateOfCreation)
	}

	val officeAddressTitle = stringResource(com.babestudios.companyinfouk.common.R.string.office_address)

	val registeredOfficeAddressString = remember(company.registeredOfficeAddress) {
		company.registeredOfficeAddress.getAddressString()
	}

	val onShowMapLambda = remember(registeredOfficeAddressString, onMapClick) {
		{ onMapClick(registeredOfficeAddressString) }
	}

	val natureOfBusinessTitle = stringResource(R.string.company_nature_of_business)
	val accountsTitle = stringResource(R.string.company_accounts)
	val filingHistoryText = stringResource(com.babestudios.companyinfouk.common.R.string.filing_history)
	val insolvencyText = stringResource(com.babestudios.companyinfouk.common.R.string.insolvency)
	val chargesText = stringResource(com.babestudios.companyinfouk.common.R.string.charges)
	val officersText = stringResource(com.babestudios.companyinfouk.common.R.string.officers)
	val personsWithControlText =
		stringResource(com.babestudios.companyinfouk.common.R.string.persons_with_significant_control)

	// These lambdas ensure that if other parts of `company` change but `companyNumber` doesn't,
	// the SingleLineCard doesn't recompose due to an unstable lambda.
	val onFilingsClickedLambda = remember(company.companyNumber, onFilingsClick) {
		{ onFilingsClick(company.companyNumber) }
	}
	val onInsolvenciesClickedLambda = remember(company.companyNumber, onInsolvenciesClick) {
		{ onInsolvenciesClick(company.companyNumber) }
	}
	val onChargesClickedLambda = remember(company.companyNumber, onChargesClick) {
		{ onChargesClick(company.companyNumber) }
	}
	val onOfficersClickedLambda = remember(company.companyNumber, onOfficersClick) {
		{ onOfficersClick(company.companyNumber) }
	}
	val onPersonsClickedLambda = remember(company.companyNumber, onPersonsClick) {
		{ onPersonsClick(company.companyNumber) }
	}

	LazyColumn(
		verticalArrangement = Arrangement.Top,
		modifier = Modifier
			.padding(paddingValues)
			.testTag("Company Screen Column")
	) {
		item {
			TitleLargeBoldText(
				text = company.companyNumber,
				modifier = Modifier
					.fillParentMaxWidth()
					.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			)
			HorizontalDivider(thickness = 1.dp)
			BodyMediumText(
				text = formattedIncorporatedOnText,
				modifier = Modifier
					.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal),
			)
			HorizontalDivider(thickness = 1.dp)
			AddressCard(
				title = officeAddressTitle,
				address = company.registeredOfficeAddress,
				onShowMap = onShowMapLambda,
			)
			HorizontalDivider(thickness = 1.dp)
			TwoLineCard(
				natureOfBusinessTitle,
				company.natureOfBusiness,
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			TwoLineCard(
				accountsTitle,
				company.lastAccountsMadeUpTo,
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			SingleLineCard(
				modifier = Modifier
					.padding(vertical = viewMarginNormal)
					.clickable(onClick = onFilingsClickedLambda),
				vectorImageResource = R.drawable.ic_company_filing_history,
				text = filingHistoryText
			)
			if (company.hasInsolvencyHistory) {
				SingleLineCard(
					modifier = Modifier
						.padding(vertical = viewMarginNormal)
						.clickable(onClick = onInsolvenciesClickedLambda),
					vectorImageResource = R.drawable.ic_company_insolvency,
					text = insolvencyText
				)
			}
			if (company.hasCharges) {
				SingleLineCard(
					modifier = Modifier
						.padding(vertical = viewMarginNormal)
						.clickable(onClick = onChargesClickedLambda),
					vectorImageResource = R.drawable.ic_company_charges,
					text = chargesText
				)
			}
			SingleLineCard(
				modifier = Modifier
					.padding(vertical = viewMarginNormal)
					.clickable(onClick = onOfficersClickedLambda),
				vectorImageResource = R.drawable.ic_company_officers,
				text = officersText
			)
			SingleLineCard(
				modifier = Modifier
					.padding(vertical = viewMarginNormal)
					.clickable(onClick = onPersonsClickedLambda),
				vectorImageResource = R.drawable.ic_company_persons_with_control,
				text = personsWithControlText
			)
			Spacer(modifier = Modifier.height(viewMarginLarge))
		}
	}

}

@PreviewLightDark
@Composable
internal fun CompanyScreenPreview(@PreviewParameter(CompanyProvider::class) company: Company) {
	CompaniesTheme {
		Box(Modifier.background(Colors.background)) {
			CompanyScreenBody(company, PaddingValues(), {}, {}, {}, {}, {}) {}
		}
	}
}

private class CompanyProvider : PreviewParameterProvider<Company> {
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
			lastAccountsMadeUpTo = "Last Full account" // made up to 31 July 2022"
		)
	)
}
