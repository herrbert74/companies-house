@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.charges.ui.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.babestudios.companyinfouk.charges.R
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.FORTY_PERCENT
import com.babestudios.companyinfouk.domain.model.charges.ChargesItem
import com.babestudios.companyinfouk.domain.model.charges.Particulars

@Composable
fun ChargeDetailsListScreen(component: ChargeDetailsComp) {

	val selectedCharge = component.selectedCharges

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_charges,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.charge_details_title)
	) {
		ChargeDetailsList(
			charge = selectedCharge,
		)
	}

}

@Composable
private fun ChargeHeader(
	modifier: Modifier = Modifier,
	charge: ChargesItem,
) {

	val viewMarginNormal = dimensionResource(com.babestudios.base.R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(com.babestudios.base.R.dimen.viewMarginLarge)

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.wrapContentHeight(Alignment.CenterVertically)
	) {
		val (
			cpnDeliveredOn, cpnStatus, cpnContainsFloatingCharge, cpnFloatingChargeCoversAll,
			cpnContainsNegativePledge, cpnContainsFixedCharge, cpnSatisfiedOn, cpnPersonsEntitled,
		) = createRefs()
		val (
			deliveredOn, status, containsFloatingCharge, floatingChargeCoversAll,
			containsNegativePledge, containsFixedCharge, satisfiedOn, personsEntitled,
		) = createRefs()
		val (divider, transaction) = createRefs()
		val guideline = createGuidelineFromEnd(FORTY_PERCENT)

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(cpnDeliveredOn) {
					top.linkTo(parent.top)
					start.linkTo(parent.start, margin = viewMarginLarge)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_delivered_on),
			style = CompaniesTypography.bodyMedium
		)
		Text(
			modifier = modifier
				.constrainAs(deliveredOn) {
					baseline.linkTo(cpnDeliveredOn.baseline)
					start.linkTo(guideline)
				},
			text = charge.deliveredOn,
			style = CompaniesTypography.titleMedium
		)
		Text(
			modifier = modifier
				.constrainAs(cpnStatus) {
					top.linkTo(deliveredOn.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start, margin = viewMarginLarge)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_status),
			style = CompaniesTypography.bodyMedium,
		)
		Text(
			modifier = modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.padding(top = viewMarginNormal, end = viewMarginLarge)
				.constrainAs(status) {
					baseline.linkTo(cpnStatus.baseline)
					start.linkTo(guideline)
					end.linkTo(parent.end, margin = viewMarginLarge)
					width = Dimension.fillToConstraints
				},
			text = charge.status,
			style = CompaniesTypography.titleMedium,
			maxLines = 3,
		)
		Text(
			modifier = modifier
				.padding(start = viewMarginLarge)
				.constrainAs(cpnContainsFloatingCharge) {
					top.linkTo(status.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_contains_floating_charge),
			style = CompaniesTypography.bodyMedium,
		)
		Text(
			modifier = modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.constrainAs(containsFloatingCharge) {
					baseline.linkTo(cpnContainsFloatingCharge.baseline)
					start.linkTo(guideline)
					end.linkTo(parent.end)
					width = Dimension.fillToConstraints
				},
			text = if (charge.particulars.containsFloatingCharge == true) "YES" else "NO",
			style = CompaniesTypography.titleMedium,
			maxLines = 3,
		)
		Text(
			modifier = modifier
				.padding(start = viewMarginLarge)
				.constrainAs(cpnFloatingChargeCoversAll) {
					top.linkTo(containsFloatingCharge.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_floating_charge_covers_all),
			style = CompaniesTypography.bodyMedium,
		)
		Text(
			modifier = modifier
				.constrainAs(floatingChargeCoversAll) {
					baseline.linkTo(cpnFloatingChargeCoversAll.baseline)
					start.linkTo(guideline)
				},
			text = if (charge.particulars.floatingChargeCoversAll == true) "YES" else "NO",
			style = CompaniesTypography.titleMedium,
			maxLines = 3,
		)
		Text(
			modifier = modifier
				.padding(start = viewMarginLarge)
				.constrainAs(cpnContainsNegativePledge) {
					top.linkTo(cpnFloatingChargeCoversAll.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_contains_negative_pledge),
			style = CompaniesTypography.bodyMedium,
		)
		Text(
			modifier = modifier
				.constrainAs(containsNegativePledge) {
					baseline.linkTo(cpnContainsNegativePledge.baseline)
					start.linkTo(guideline)
				},
			text = if (charge.particulars.containsNegativePledge == true) "YES" else "NO",
			style = CompaniesTypography.titleMedium
		)
		Text(
			modifier = modifier
				.constrainAs(cpnContainsFixedCharge) {
					top.linkTo(containsNegativePledge.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start, margin = viewMarginLarge)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_contains_fixed_charge),
			style = CompaniesTypography.bodyMedium,
		)
		Text(
			modifier = modifier
				.constrainAs(containsFixedCharge) {
					baseline.linkTo(cpnContainsFixedCharge.baseline)
					start.linkTo(guideline)
				},
			text = if (charge.particulars.containsFixedCharge == true) "YES" else "NO",
			style = CompaniesTypography.titleMedium
		)
		Text(
			modifier = modifier
				.constrainAs(cpnSatisfiedOn) {
					top.linkTo(containsFixedCharge.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start, margin = viewMarginLarge)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_satisfied_on),
			style = CompaniesTypography.bodyMedium
		)
		Text(
			modifier = modifier
				.constrainAs(satisfiedOn) {
					baseline.linkTo(cpnSatisfiedOn.baseline)
					start.linkTo(guideline)
				},
			text = charge.satisfiedOn,
			style = CompaniesTypography.titleMedium,
		)
		Text(
			modifier = modifier
				.constrainAs(cpnPersonsEntitled) {
					top.linkTo(satisfiedOn.bottom, margin = viewMarginNormal)
					start.linkTo(parent.start, margin = viewMarginLarge)
					end.linkTo(guideline, margin = viewMarginNormal)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.charge_details_persons_entitled),
			style = CompaniesTypography.bodyMedium
		)
		Text(
			modifier = modifier
				.constrainAs(personsEntitled) {
					baseline.linkTo(cpnPersonsEntitled.baseline)
					start.linkTo(guideline)
					end.linkTo(parent.end)
					width = Dimension.fillToConstraints
				},
			text = charge.personsEntitled,
			style = CompaniesTypography.titleMedium,
			maxLines = 3,
		)
		Divider(
			modifier = modifier
				.constrainAs(divider) {
					top.linkTo(personsEntitled.bottom, margin = viewMarginLarge)
					width = Dimension.fillToConstraints
				},
		)
		Text(
			modifier = modifier
				.padding(bottom = viewMarginLarge)
				.constrainAs(transaction) {
					start.linkTo(parent.start, margin = viewMarginLarge)
					top.linkTo(divider.bottom, margin = viewMarginLarge)
					width = Dimension.fillToConstraints
				},
			text = stringResource(R.string.transactions),
			style = CompaniesTypography.titleMedium,
		)
	}
}

@Composable
private fun ChargeDetailsList(
	charge: ChargesItem,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(charge.transactions) { index, Transaction ->
				if (index == 0) {
					ChargeHeader(charge = charge)
				}
				TransactionListItem(
					item = Transaction,
				)

				Divider()

			}
		}

	}
}

@Preview("Charge Header Preview")
@Composable
fun ChargeHeaderPreview() {
	ChargeHeader(
		charge = ChargesItem(
			chargeCode = "2",
			status = "Outstanding",
			deliveredOn = "2011-01-13",
			satisfiedOn = "2012-01-13",
			createdOn = "2011-01-10",
			particulars = Particulars(
				type = "short-particulars",
				containsFixedCharge = true,
				floatingChargeCoversAll = false,
				containsFloatingCharge = true,
				containsNegativePledge = true,
				description = "The subcontracts relating to the vehicles or other goods now or hereafter owned by the lessor" +
					" and comprised in the principal contracts, the full benefit of all monies under the subcontracts, the " +
					"benefit of all guarantees, the benefit of all insurances, the benefit of all supplemental or collateral " +
					"agreements, see image for full details."
			),
			personsEntitled = "Man Financial Services PLC",
		)
	)
}
