package com.babestudios.companyinfouk.charges.ui.charges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Visibility
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem

@Composable
internal fun ChargesItemListItem(
	item: ChargesItem,
	modifier: Modifier = Modifier,
	onItemClicked: (id: ChargesItem) -> Unit,
) {

	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)
	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)
	val viewMarginSmall = dimensionResource(R.dimen.viewMarginSmall)

	val painter = if (item.status == "Satisfied") {
		painterResource(R.drawable.ic_baseline_sentiment_satisfied)
	} else {
		painterResource(R.drawable.ic_baseline_sentiment_very_dissatisfied)
	}

	val colorFilter = if (item.status == "Satisfied") {
		ColorFilter.tint(colorResource(R.color.green))
	} else {
		ColorFilter.tint(colorResource(R.color.red))
	}

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically)
			.clickable { onItemClicked(item) },
	) {

		val (cpnCreatedOn, cpnSatisfiedOn, cpnChargeCode, cpnPersonsEntitled) = createRefs()
		val (createdOn, satisfiedOn, chargeCode, personsEntitled, status) = createRefs()

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(cpnChargeCode) {
					top.linkTo(parent.top, margin = viewMarginNormal)
					bottom.linkTo(chargeCode.top)
					linkTo(
						parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f
					)
				},
			text = stringResource(R.string.charge_details_charge_code),
			style = CompaniesTypography.bodyMedium
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
			style = CompaniesTypography.titleMedium
		)

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(cpnCreatedOn) {
					linkTo(parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f)
					top.linkTo(chargeCode.bottom, margin = viewMarginNormal)
					bottom.linkTo(createdOn.top)
				},
			text = stringResource(R.string.charge_details_created_on),
			style = CompaniesTypography.bodyMedium
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
			style = CompaniesTypography.titleMedium
		)

		val satisfiedVisibility = if (item.status == "Satisfied") Visibility.Visible else Visibility.Gone
		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(cpnSatisfiedOn) {
					top.linkTo(createdOn.bottom, margin = viewMarginNormal)
					bottom.linkTo(satisfiedOn.top)
					linkTo(
						parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f
					)
					visibility = satisfiedVisibility
				},
			text = stringResource(R.string.charge_details_satisfied_on),
			style = CompaniesTypography.bodyMedium
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
			style = CompaniesTypography.titleMedium
		)

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(cpnPersonsEntitled) {
					linkTo(parent.start, status.start, viewMarginLarge + viewMarginSmall, viewMarginNormal, bias = 0f)
					top.linkTo(satisfiedOn.bottom, margin = viewMarginNormal, goneMargin = viewMarginNormal)
					bottom.linkTo(personsEntitled.top)
				},
			text = stringResource(R.string.charge_details_persons_entitled),
			style = CompaniesTypography.bodyMedium
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
			style = CompaniesTypography.titleMedium
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

interface ScrollbarAdapter

@Suppress("UNUSED_PARAMETER")
@Composable
fun rememberScrollbarAdapter(
	scrollState: LazyListState,
	itemCount: Int,
	averageItemSize: Dp,
): ScrollbarAdapter =
	object : ScrollbarAdapter {}

@Suppress("UNUSED_PARAMETER")
@Composable
fun VerticalScrollbar(
	modifier: Modifier,
	adapter: ScrollbarAdapter,
) {
	//no-op
}

@Preview("Satisfied Item Preview")
@Composable
fun SatisfiedItemPreview() {
	ChargesItemListItem(
		ChargesItem(
			createdOn = "2012-03-12",
			satisfiedOn = "2013-03-12",
			chargeCode = "095699920001",
			personsEntitled = "John Doe",
			status = "Satisfied"
		),
		onItemClicked = {}
	)
}

@Preview("Dissatisfied Item Preview")
@Composable
fun DissatisfiedItemPreview() {
	ChargesItemListItem(
		ChargesItem(
			createdOn = "2012-03-12",
			chargeCode = "095699920001",
			personsEntitled = "John Doe",
			status = "Outstanding"
		),
		onItemClicked = {}
	)
}
