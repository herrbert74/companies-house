package com.babestudios.companyinfouk.insolvencies.ui.practitioner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R
import kotlinx.coroutines.Dispatchers

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun PractitionerDetailsScreen(component: PractitionerDetailsComp) {

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val selectedPractitionerDetails = component.selectedPractitioner

	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = "Practitioner Details",
						style = CompaniesTypography.titleLarge,
					)
				},
				navigationIcon = {
					IconButton(onClick = { component.onBackClicked() }) {
						Icon(
							imageVector = Icons.Filled.ArrowBack,
							contentDescription = "Localized description"
						)
					}
				},
				//Add back image background once supported
				//app:imageViewSrc="@drawable/bg_insolvencies"
				//scrollBehavior = scrollBehavior
			)
		},

		content = { innerPadding ->
			Column(
				verticalArrangement = Arrangement.Top,
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.padding(innerPadding)
					.verticalScroll(state)
					.background(color = MaterialTheme.colors.background),
			) {
				TwoLineCard(
					firstLineString = stringResource(R.string.insolvency_details_name),
					secondLineString = selectedPractitionerDetails.name,
				)
				Divider(thickness = 1.dp)
				selectedPractitionerDetails.appointedOn?.let {
					TwoLineCard(
						firstLineString = stringResource(R.string.insolvency_details_appointed_on),
						secondLineString = it,
					)
					Divider(thickness = 1.dp)
				}
				TwoLineCard(
					firstLineString = stringResource(R.string.insolvency_details_address),
					secondLineString = selectedPractitionerDetails.address.addressLine1,
				)
				selectedPractitionerDetails.address.addressLine2?.let {
					Text(
						modifier = Modifier
							.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
							.fillMaxWidth(1f),
						text = it,
						style = CompaniesTypography.titleLargeBold,
					)
				}
				Text(
					modifier = Modifier
						.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
						.fillMaxWidth(1f),
					text = selectedPractitionerDetails.address.locality,
					style = CompaniesTypography.titleLargeBold,
				)
				Text(
					modifier = Modifier
						.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
						.fillMaxWidth(1f),
					text = selectedPractitionerDetails.address.postalCode,
					style = CompaniesTypography.titleLargeBold,
				)
				selectedPractitionerDetails.address.region?.let {
					Text(
						modifier = Modifier
							.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
							.fillMaxWidth(1f),
						text = it,
						style = CompaniesTypography.titleLargeBold,
					)
				}
				selectedPractitionerDetails.address.country?.let {
					Text(
						modifier = Modifier
							.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
							.fillMaxWidth(1f),
						text = it,
						style = CompaniesTypography.titleLargeBold,
					)
				}
			}
		}
	)

}

@Preview("PractitionerDetails Preview")
@Composable
fun PractitionerDetailsScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		PractitionerDetailsScreen(
			PractitionerDetailsComponent(
				componentContext,
				Dispatchers.Main,
				Practitioner(
					name = "John Doe",
					address = Address(
						addressLine1 = "Suite A",
						addressLine2 = "4-6 Canfield Place",
						locality = "London",
						postalCode = "NW6 3BT",
						country = "United Kingdom"
					),
					appointedOn = "2016-02-26",
					ceasedToActOn = "2017-02-26",
					role = "practitioner"
				),
			) { }
		)
	}
}
