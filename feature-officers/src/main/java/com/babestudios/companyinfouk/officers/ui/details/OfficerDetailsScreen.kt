package com.babestudios.companyinfouk.officers.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.babestudios.companyinfouk.common.compose.AddressCard
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.domain.PREVIEW_MONTH
import com.babestudios.companyinfouk.domain.PREVIEW_YEAR
import com.babestudios.companyinfouk.domain.model.common.Address
import com.babestudios.companyinfouk.domain.model.common.MonthYear
import com.babestudios.companyinfouk.domain.model.officers.Officer
import com.babestudios.companyinfouk.officers.R
import kotlinx.coroutines.Dispatchers

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun OfficerDetailsScreen(component: OfficerDetailsComp) {

	val viewMarginNormal = Dimens.marginNormal

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val selectedOfficer = component.selectedOfficer

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_officers,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.officer_details)
	) {
		Column(
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.verticalScroll(state)
		) {
			TwoLineCard(
				firstLineString = "Name",
				secondLineString = selectedOfficer.name,
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			TwoLineCard(
				firstLineString = stringResource(R.string.officer_details_appointed_on),
				secondLineString = selectedOfficer.appointedOn ?: "Unknown",
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			selectedOfficer.resignedOn?.let {
				TwoLineCard(
					firstLineString = stringResource(R.string.officer_appointments_resigned_on),
					secondLineString = it,
					Modifier.fillMaxWidth(1f)
				)
				Divider(thickness = 1.dp)
			}
			TwoLineCard(
				firstLineString = "Nationality",
				secondLineString = selectedOfficer.nationality,
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			TwoLineCard(
				firstLineString = stringResource(R.string.officer_details_occupation),
				secondLineString = selectedOfficer.occupation,
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			val (month, year) = selectedOfficer.dateOfBirth.month to selectedOfficer.dateOfBirth.year
			TwoLineCard(
				firstLineString = "Date of birth",
				secondLineString = if (month == null || year == null) {
					"Unknown"
				} else {
					"$month / $year"
				},
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			TwoLineCard(
				firstLineString = "Country of residence",
				secondLineString = selectedOfficer.countryOfResidence,
				Modifier.fillMaxWidth(1f)
			)
			Divider(thickness = 1.dp)
			AddressCard(address = selectedOfficer.address) { component.onShowMapClicked() }
			Divider(thickness = 1.dp)
			Button(
				onClick = { component.onAppointmentsClicked() },
				modifier = Modifier
					.padding(
						top = viewMarginNormal,
						bottom = viewMarginNormal,
						end = viewMarginNormal,
					),
			) {
				Text(text = stringResource(R.string.officer_details_appointments).uppercase())
			}
		}
	}

}

@Preview("Officer Details Preview")
@Composable
fun OfficerDetailsScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
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
				dateOfBirth = MonthYear(PREVIEW_YEAR, PREVIEW_MONTH),
				occupation = "Director",
				nationality = "German",
				countryOfResidence = "United Kingdom",
				officerRole = "director",
				appointedOn = "2012-08-14",
				resignedOn = "2015-08-14",
				appointmentsId = "6fsh143wgC_U_M4LV9DfpGKskM0",
				fromToString = "From 2012-08-14"
			)
		) { }
	)
}

@Preview("Officer Details Default Preview")
@Composable
fun OfficerDetailsScreenDefaultsPreview() {
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
					dateOfBirth = MonthYear(PREVIEW_YEAR, PREVIEW_MONTH),
					occupation = "Director",
					nationality = "German",
					countryOfResidence = "United Kingdom",
					officerRole = "director",
					appointedOn = "2012-08-14",
					appointmentsId = "6fsh143wgC_U_M4LV9DfpGKskM0",
					fromToString = "From 2012-08-14"
				)
			) { }
		)
	}
}
