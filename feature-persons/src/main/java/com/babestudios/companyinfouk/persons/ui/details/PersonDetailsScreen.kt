package com.babestudios.companyinfouk.persons.ui.details

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
import com.babestudios.companyinfouk.persons.R
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.persons.Identification
import com.babestudios.companyinfouk.shared.domain.model.persons.Person
import com.babestudios.companyinfouk.shared.screen.persondetails.PersonDetailsComp
import com.babestudios.companyinfouk.shared.screen.persondetails.PersonDetailsComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth

@Composable
@Suppress("LongMethod", "ComplexMethod")
fun PersonDetailsScreen(
	component: PersonDetailsComp,
	modifier: Modifier = Modifier,
) {

	TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
	val selectedPerson = component.selectedPerson

	BackHandler(onBack = { component.onBackClicked() })
	val state = rememberScrollState()

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_persons,
		onBackClick = { component.onBackClicked() },
		title = stringResource(id = R.string.person_details)
	) { paddingValues ->
		Column(
			verticalArrangement = Arrangement.Top,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = modifier
				.verticalScroll(state)
				.padding(paddingValues)
		) {
			TwoLineCard(
				firstLineString = "Name",
				secondLineString = selectedPerson.name,
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			if (selectedPerson.notifiedOn.isNotBlank()) {
				TwoLineCard(
					firstLineString = "Notified on",
					secondLineString = selectedPerson.notifiedOn,
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			TwoLineCard(
				firstLineString = "Kind",
				secondLineString = selectedPerson.kind,
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			TwoLineCard(
				firstLineString = "Natures of control",
				secondLineString = selectedPerson.naturesOfControl.joinToString(separator = "\n"),
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			TwoLineCard(
				firstLineString = "Nationality",
				secondLineString = selectedPerson.nationality ?: "Unknown",
				Modifier.fillMaxWidth(1f)
			)
			HorizontalDivider(thickness = 1.dp)
			val (month, year) = selectedPerson.dateOfBirth.month to selectedPerson.dateOfBirth.year
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
			if ((selectedPerson.countryOfResidence?.isBlank() == false
					&& selectedPerson.countryOfResidence != selectedPerson.address.country)
			) {
				TwoLineCard(
					firstLineString = "Country of residence",
					secondLineString = selectedPerson.countryOfResidence ?: "",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			if (selectedPerson.identification?.placeRegistered?.isBlank() == false) {
				TwoLineCard(
					firstLineString = "Place registered",
					secondLineString = selectedPerson.identification?.placeRegistered ?: "",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			if (selectedPerson.identification?.registrationNumber?.isBlank() == false) {
				TwoLineCard(
					firstLineString = "Registration number",
					secondLineString = selectedPerson.identification?.registrationNumber ?: "",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			if (selectedPerson.identification?.legalAuthority?.isBlank() == false) {
				TwoLineCard(
					firstLineString = "Legal authority",
					secondLineString = selectedPerson.identification?.legalAuthority ?: "",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			if (selectedPerson.identification?.legalForm?.isBlank() == false) {
				TwoLineCard(
					firstLineString = "Legal form",
					secondLineString = selectedPerson.identification?.legalForm ?: "",
					Modifier.fillMaxWidth(1f)
				)
				HorizontalDivider(thickness = 1.dp)
			}
			AddressCard(address = selectedPerson.address) { component.onShowMapClicked() }
		}
	}

}

@PreviewLightDark
@Composable
private fun PersonDetailsScreenPreview() {
	val componentContext = DefaultComponentContext(lifecycle = LifecycleRegistry())
	CompaniesTheme {
		PersonDetailsScreen(
			PersonDetailsComponent(
				componentContext,
				Dispatchers.Main,
				Person(
					name = "Robert Who",
					notifiedOn = "2016-04-06",
					address = Address(
						addressLine1 = "10 South Lane",
						locality = "Elland",
						postalCode = "HX5 0HQ",
						region = "West Yorkshire"
					),
					nationality = "British",
					countryOfResidence = "England",
					dateOfBirth = YearMonth(2, 1),
					kind = "Individual",
					naturesOfControl =
						listOf("Ownership of shares - 75% or more", "Ownership of voting rights - 75% or more"),
					identification = Identification(
						placeRegistered = "England",
						legalAuthority = "Limited Company, England And Wales", legalForm = "Limited Company"
					)
				)
			) { }
		)
	}
}
