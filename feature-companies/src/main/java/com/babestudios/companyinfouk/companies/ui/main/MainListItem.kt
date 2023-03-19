package com.babestudios.companyinfouk.companies.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.babestudios.base.R
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.search.CompanySearchResultItem

@Composable
internal fun CompanySearchResultItemListItem(
	item: CompanySearchResultItem,
	modifier: Modifier = Modifier,
	onItemClicked: (id: CompanySearchResultItem) -> Unit,
) {

	val viewMarginNormal = dimensionResource(R.dimen.viewMargin)
	val viewMarginLarge = dimensionResource(R.dimen.viewMarginLarge)

	ConstraintLayout(
		modifier = modifier
			.fillMaxWidth(1f)
			.clickable { onItemClicked(item) },
	) {
		val (title, description, addressSnippet, companyStatus) = createRefs()

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(title) {
					width = Dimension.fillToConstraints
					linkTo(parent.top, description.top, viewMarginNormal, 0.dp)
					linkTo(parent.start, companyStatus.start, viewMarginLarge, 0.dp, bias = 0f)
				},
			text = item.title ?: "",
			style = CompaniesTypography.titleMedium,
			maxLines = 2
		)
		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(description) {
					width = Dimension.fillToConstraints
					linkTo(title.bottom, addressSnippet.top, viewMarginNormal, 0.dp)
					linkTo(parent.start, companyStatus.start, viewMarginLarge, 0.dp)
				},
			text = item.description ?: "",
			style = CompaniesTypography.bodyMedium,
			maxLines = 2
		)
		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.constrainAs(addressSnippet) {
					width = Dimension.fillToConstraints
					linkTo(parent.start, companyStatus.start, viewMarginLarge, 0.dp)
					linkTo(description.bottom, parent.bottom, viewMarginNormal, viewMarginNormal)
				},
			text = item.addressSnippet ?: "",
			style = CompaniesTypography.bodyMedium,
			maxLines = 3,
		)
		Text(
			modifier = modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.padding(start = viewMarginNormal)
				.constrainAs(companyStatus) {
					linkTo(parent.top, parent.bottom, viewMarginNormal, viewMarginNormal)
					end.linkTo(parent.end, viewMarginLarge)
				},
			text = item.companyStatus ?: "",
			style = CompaniesTypography.bodyMedium,
		)
	}
}

@Preview("Item Preview")
@Composable
fun DefaultPreview() {
	CompanySearchResultItemListItem(
		modifier = Modifier,
		item = CompanySearchResultItem(
			title = "ALPHABET ACCOUNTANTS LTD",
			description = "07620277 - Incorporated on  3 May 2011",
			addressSnippet = "16  Anchor Street, Chelmsford, CM2 0JY",
			companyStatus = "active"
		),
		onItemClicked = {}
	)
}

@Preview("Small Preview", device = "id:Nexus S")
@Composable
fun SmallPreview() {
	CompanySearchResultItemListItem(
		modifier = Modifier,
		item = CompanySearchResultItem(
			title = "ALPHABET ACCOUNTANTS LTD",
			description = "07620277 - Incorporated on  3 May 2011",
			addressSnippet = "16  Anchor Street, Chelmsford, CM2 0JY",
			companyStatus = "active"
		),
		onItemClicked = {}
	)
}
