package com.babestudios.companyinfouk.officers.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.officers.R
import com.babestudios.companyinfouk.shared.domain.PREVIEW_MONTH
import com.babestudios.companyinfouk.shared.domain.PREVIEW_YEAR
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.officers.Officer
import com.babestudios.companyinfouk.shared.screen.officerdetails.OfficerDetailsComp
import com.babestudios.companyinfouk.shared.screen.officerdetails.OfficerDetailsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun OfficerDetailsScreen(
	component: OfficerDetailsComp,
	modifier: Modifier = Modifier,
) {
	val viewMarginNormal = Dimens.marginNormal

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val selectedOfficer = component.selectedOfficer

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_officers,
		onBackClick = { component.onBackClicked() },
		title = stringResource(R.string.officer_details)
	) { paddingValues ->
		LazyColumn(
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = modifier
				.padding(paddingValues)
				.testTag("OfficerDetailsColumn")
		) {
			item {
				TwoLineCard(
					firstLineString = "Name",
					secondLineString = selectedOfficer.name,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				TwoLineCard(
					firstLineString = stringResource(R.string.officer_details_appointed_on),
					secondLineString = selectedOfficer.appointedOn ?: "Unknown",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			selectedOfficer.resignedOn?.let {
				item {
					TwoLineCard(
						firstLineString = stringResource(R.string.officer_appointments_resigned_on),
						secondLineString = it,
						Modifier.fillMaxWidth(1f)
					)
					HorizontalDivider(thickness = 1.dp)
				}
			}
			item {
				TwoLineCard(
					firstLineString = "Nationality",
					secondLineString = selectedOfficer.nationality,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				TwoLineCard(
					firstLineString = stringResource(R.string.officer_details_occupation),
					secondLineString = selectedOfficer.occupation,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				val (month, year) = selectedOfficer.dateOfBirth.month to selectedOfficer.dateOfBirth.year
				TwoLineCard(
					firstLineString = "Date of birth",
					secondLineString = if (year == 0 && month == Month(1)) {
						"Unknown"
					} else {
						"$month / $year"
					},
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				TwoLineCard(
					firstLineString = "Country of residence",
					secondLineString = selectedOfficer.countryOfResidence,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				AddressCard(address = selectedOfficer.address) { component.onShowMapClicked() }
				HorizontalDivider(thickness = 1.dp)
			}
			item {
				Button(
					onClick = { component.onAppointmentsClicked() },
					modifier = Modifier
						.padding(
							top = viewMarginNormal,
							bottom = viewMarginNormal,
						),
				) {
					Text(text = stringResource(R.string.officer_details_appointments).uppercase())
				}
			}
		}
	}

}

@PreviewLightDark
@Composable
private fun OfficerDetailsScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		OfficerDetailsScreen(
			OfficerDetailsComponent(
				componentContext,
				Dispatchers.Main,
				Officer(
					name = "ALDERSEY, Scherin Abada",
					address = Address(
						addressLine1 = "Suite A",
						addressLine2 = "4-6 Canfield Place",
						locality = "London",
						postalCode = "NW6 3BT",
					),
					dateOfBirth = YearMonth(PREVIEW_YEAR, PREVIEW_MONTH),
					occupation = "Director",
					nationality = "German",
					countryOfResidence = "United Kingdom",
					officerRole = "director",
					appointedOn = "2012-08-14",
					resignedOn = "2015-08-14",
					appointmentsId = "6fsh143wgC_U_M4LV9DfpGKskM0",
					fromToString = "From 2012-08-14"
				)
			) { },
			Modifier
		)
	}
}

@Preview
@Composable
private fun OfficerDetailsScreenUnknownBirthdayPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		OfficerDetailsScreen(
			OfficerDetailsComponent(
				componentContext,
				Dispatchers.Main,
				Officer(
					name = "ALDERSEY, Scherin Abada",
					address = Address(
						addressLine1 = "Suite A",
						addressLine2 = "4-6 Canfield Place",
						locality = "London",
						postalCode = "NW6 3BT",
					),
					dateOfBirth = YearMonth(0, 1),
					occupation = "Director",
					nationality = "German",
					countryOfResidence = "United Kingdom",
					officerRole = "director",
					appointedOn = "2012-08-14",
					resignedOn = "2015-08-14",
					appointmentsId = "6fsh143wgC_U_M4LV9DfpGKskM0",
					fromToString = "From 2012-08-14"
				)
			) { },
			Modifier
		)
	}
}
