package com.babestudios.companyinfouk.insolvencies.ui.practitioner

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.insolvencies.R
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.shared.screen.practitionerdetails.PractitionerDetailsComp
import com.babestudios.companyinfouk.shared.screen.practitionerdetails.PractitionerDetailsComponent
import kotlinx.coroutines.Dispatchers

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun PractitionerDetailsScreen(component: PractitionerDetailsComp) {

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val selectedPractitionerDetails = component.selectedPractitioner

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_insolvency,
		title = stringResource(R.string.practitioner_details),
		onBackClicked = { component.onBackClicked() },
	) { paddingValues ->
		Column(
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.verticalScroll(state)
				.padding(paddingValues),
		) {
			TwoLineCard(
				firstLineString = stringResource(R.string.insolvency_details_name),
				secondLineString = selectedPractitionerDetails.name,
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			selectedPractitionerDetails.appointedOn?.let {
				TwoLineCard(
					firstLineString = stringResource(R.string.insolvency_details_appointed_on),
					secondLineString = it,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			AddressCard(address = selectedPractitionerDetails.address) {
				component.onShowMapClicked()
			}
		}
	}

}

@PreviewLightDark
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
