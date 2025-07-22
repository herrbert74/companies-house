package com.babestudios.companyinfouk.charges.ui.charges

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.charges.ChargesItem

@Composable
internal fun ChargesItemListItem(
	item: ChargesItem,
	modifier: Modifier = Modifier,
	onItemClick: (id: ChargesItem) -> Unit,
) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal
	val viewMarginSmall = Dimens.marginSmall

	val painter = if (item.status == "Satisfied") {
		painterResource(R.drawable.ic_baseline_sentiment_satisfied)
	} else {
		painterResource(R.drawable.ic_baseline_sentiment_very_dissatisfied)
	}

	val colorFilter = if (item.status == "Satisfied") {
		ColorFilter.tint(colorResource(com.babestudios.companyinfouk.common.R.color.green))
	} else {
		ColorFilter.tint(colorResource(com.babestudios.companyinfouk.common.R.color.red))
	}

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically)
			.clickable { onItemClick(item) },
	) {
		val (cpnCreatedOn, cpnSatisfiedOn, cpnChargeCode, cpnPersonsEntitled) = createRefs()
		val (createdOn, satisfiedOn, chargeCode, personsEntitled, status) = createRefs()

		Text(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.constrainAs(cpnChargeCode) {
					top.linkTo(parent.top, margin = viewMarginNormal)
					bottom.linkTo(chargeCode.top)
					linkTo(
						parent.start,
						status.start,
						viewMarginLarge + viewMarginSmall,
						viewMarginNormal,
						bias = 0f
					)
				},
			text = stringResource(R.string.charge_details_charge_code),
			style = CompaniesTypography.bodyMedium.merge(Colors.onBackground)
		)

		Text(
			text = AnnotatedString(item.chargeCode),
			modifier = Modifier
				.constrainAs(chargeCode) {
					linkTo(parent.start, status.start, viewMarginLarge, viewMarginNormal, bias = 0f)
					top.linkTo(cpnChargeCode.bottom, margin = viewMarginSmall)
					bottom.linkTo(cpnCreatedOn.top)
				},
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium.merge(Colors.onBackground)
		)

		Text(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.constrainAs(cpnCreatedOn) {
					linkTo(parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f)
					top.linkTo(chargeCode.bottom, margin = viewMarginNormal)
					bottom.linkTo(createdOn.top)
				},
			text = stringResource(R.string.charge_details_created_on),
			style = CompaniesTypography.bodyMedium.merge(Colors.onBackground)
		)

		Text(
			text = AnnotatedString(item.createdOn),
			modifier = Modifier
				.constrainAs(createdOn) {
					linkTo(parent.start, status.start, viewMarginLarge, viewMarginNormal, bias = 0f)
					top.linkTo(cpnCreatedOn.bottom, margin = viewMarginSmall)
					bottom.linkTo(cpnSatisfiedOn.top)
				},
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium.merge(Colors.onBackground)
		)

		val satisfiedVisibility = if (item.status == "Satisfied") Visibility.Visible else Visibility.Gone
		Text(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.constrainAs(cpnSatisfiedOn) {
					top.linkTo(createdOn.bottom, margin = viewMarginNormal)
					bottom.linkTo(satisfiedOn.top)
					linkTo(
						parent.start,
						status.start,
						viewMarginLarge + viewMarginSmall,
						viewMarginNormal,
						bias = 0f
					)
					visibility = satisfiedVisibility
				},
			text = stringResource(R.string.charge_details_satisfied_on),
			style = CompaniesTypography.bodyMedium.merge(Colors.onBackground)
		)

		Text(
			text = AnnotatedString(item.satisfiedOn),
			modifier = Modifier
				.constrainAs(satisfiedOn) {
					linkTo(parent.start, status.start, viewMarginLarge, viewMarginNormal, bias = 0f)
					top.linkTo(cpnSatisfiedOn.bottom, margin = viewMarginSmall)
					bottom.linkTo(cpnPersonsEntitled.top)
					visibility = satisfiedVisibility
				},
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium.merge(Colors.onBackground)
		)

		Text(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.constrainAs(cpnPersonsEntitled) {
					linkTo(parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f)
					top.linkTo(satisfiedOn.bottom, margin = viewMarginNormal, goneMargin = viewMarginNormal)
					bottom.linkTo(personsEntitled.top)
				},
			text = stringResource(R.string.charge_details_persons_entitled),
			style = CompaniesTypography.bodyMedium.merge(Colors.onBackground)
		)

		Text(
			text = AnnotatedString(item.personsEntitled),
			modifier = Modifier
				.constrainAs(personsEntitled) {
					linkTo(parent.start, status.start, viewMarginLarge, viewMarginNormal, bias = 0f)
					top.linkTo(cpnPersonsEntitled.bottom, margin = viewMarginSmall)
					bottom.linkTo(parent.bottom, margin = viewMarginNormal)
				},
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium.merge(Colors.onBackground)
		)

		Image(
			painter = painter,
			contentDescription = "status",
			modifier = Modifier
				.padding(start = viewMarginNormal, end = viewMarginLarge)
				.constrainAs(status) {
					top.linkTo(parent.top)
					bottom.linkTo((parent.bottom))
					end.linkTo(parent.end)
				},
			alignment = Alignment.CenterEnd,
			ContentScale.FillBounds,
			colorFilter = colorFilter
		)
	}
}

@Preview("Satisfied Charges Item Preview")
@Composable
private fun SatisfiedItemPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			ChargesItemListItem(
				ChargesItem(
					createdOn = "2012-03-12",
					satisfiedOn = "2013-03-12",
					chargeCode = "095699920001",
					personsEntitled = "John Doe",
					status = "Satisfied"
				),
				onItemClick = {}
			)
		}
	}
}

@Preview("Satisfied Charges Item Dark Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SatisfiedItemDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			ChargesItemListItem(
				ChargesItem(
					createdOn = "2012-03-12",
					satisfiedOn = "2013-03-12",
					chargeCode = "095699920001",
					personsEntitled = "John Doe",
					status = "Satisfied"
				),
				onItemClick = {}
			)
		}
	}
}

@Preview("Dissatisfied Charges Item Preview")
@Composable
private fun DissatisfiedItemPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			ChargesItemListItem(
				ChargesItem(
					createdOn = "2012-03-12",
					chargeCode = "095699920001",
					personsEntitled = "John Doe",
					status = "Outstanding"
				),
				onItemClick = {}
			)
		}
	}
}

@Preview("Dissatisfied Charges Item Dark Preview", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DissatisfiedItemDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			ChargesItemListItem(
				ChargesItem(
					createdOn = "2012-03-12",
					chargeCode = "095699920001",
					personsEntitled = "John Doe",
					status = "Outstanding"
				),
				onItemClick = {}
			)
		}
	}
}
