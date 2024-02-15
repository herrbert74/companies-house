package com.babestudios.companyinfouk.officers.ui.appointments

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.BodyMediumText
import com.babestudios.companyinfouk.design.component.TitleMediumText
import com.babestudios.companyinfouk.shared.domain.FORTY_PERCENT
import com.babestudios.companyinfouk.shared.domain.model.officers.AppointedTo
import com.babestudios.companyinfouk.shared.domain.model.officers.Appointment
import com.babestudios.companyinfouk.officers.R

@Composable
internal fun AppointmentListItem(
	item: Appointment,
	modifier: Modifier = Modifier,
	onItemClicked: (id: Appointment) -> Unit,
) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically)
			.clickable { onItemClicked(item) },
	) {
		val (cpnAppointedOn, cpnCompanyName, cpnCompanyStatus, cpnRole, cpnResignedOn) = createRefs()
		val (appointedOn, companyName, companyStatus, role, resignedOn) = createRefs()
		val guideline = createGuidelineFromStart(FORTY_PERCENT)

		BodyMediumText(
			textAlign = TextAlign.Start,
			modifier = modifier
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal)
				.constrainAs(cpnAppointedOn) {
					top.linkTo(parent.top)
					start.linkTo(parent.start)
				},
			text = stringResource(R.string.officer_details_appointed_on),
		)
		TitleMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal)
				.constrainAs(appointedOn) {
					baseline.linkTo(cpnAppointedOn.baseline)
					start.linkTo(guideline)
				},
			text = item.appointedOn ?: "",
		)
		BodyMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, bottom = viewMarginNormal)
				.constrainAs(cpnCompanyName) {
					top.linkTo(appointedOn.bottom)
					start.linkTo(parent.start)
				},
			text = "Company Name",
		)
		TitleMediumText(
			modifier = modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.padding(start = viewMarginLarge, bottom = viewMarginNormal, end = viewMarginLarge)
				.constrainAs(companyName) {
					baseline.linkTo(cpnCompanyName.baseline)
					start.linkTo(guideline)
					end.linkTo(parent.end)
					width = Dimension.fillToConstraints
				},
			text = item.appointedTo.companyName,
		)
		BodyMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, bottom = viewMarginNormal)
				.constrainAs(cpnCompanyStatus) {
					top.linkTo(companyName.bottom)
					start.linkTo(parent.start)
				},
			text = "Company Status",
		)
		TitleMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, bottom = viewMarginNormal)
				.constrainAs(companyStatus) {
					baseline.linkTo(cpnCompanyStatus.baseline)
					start.linkTo(guideline)
				},
			text = item.appointedTo.companyStatus,
		)
		BodyMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, bottom = viewMarginNormal)
				.constrainAs(cpnRole) {
					top.linkTo(cpnCompanyStatus.bottom)
					start.linkTo(parent.start)
				},
			text = stringResource(R.string.officer_appointments_role),
		)
		TitleMediumText(
			modifier = modifier
				.padding(start = viewMarginLarge, bottom = viewMarginNormal)
				.constrainAs(role) {
					baseline.linkTo(cpnRole.baseline)
					start.linkTo(guideline)
				},
			text = item.officerRole,
		)
		if (item.resignedOn != null) {
			BodyMediumText(
				modifier = modifier
					.padding(start = viewMarginLarge, bottom = viewMarginNormal)
					.constrainAs(cpnResignedOn) {
						top.linkTo(cpnRole.bottom)
						start.linkTo(parent.start)
						bottom.linkTo(parent.bottom)
					},
				text = stringResource(R.string.officer_appointments_resigned_on),
			)
			TitleMediumText(
				modifier = modifier
					.padding(start = viewMarginLarge, bottom = viewMarginNormal)
					.constrainAs(resignedOn) {
						baseline.linkTo(cpnResignedOn.baseline)
						start.linkTo(guideline)
					},
				text = item.resignedOn!!,
			)
		}
	}
}

@Preview
@Composable
fun AppointmentListItemPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			AppointmentListItem(
				Appointment(
					appointedOn = "2012-8-12",
					appointedTo = AppointedTo(
						companyName = "Heart Foundation Multiple Lines Too Long", companyStatus = "active"
					),
					officerRole = "Director",
					resignedOn = "2013-10-12"
				),
				onItemClicked = {}
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppointmentListItemDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			AppointmentListItem(
				Appointment(
					appointedOn = "2012-8-12",
					appointedTo = AppointedTo(
						companyName = "Heart Foundation Multiple Lines Too Long", companyStatus = "active"
					),
					officerRole = "Director",
					resignedOn = "2013-10-12"
				),
				onItemClicked = {}
			)
		}
	}
}
