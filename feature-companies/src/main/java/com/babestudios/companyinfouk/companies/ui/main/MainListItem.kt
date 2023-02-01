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
import androidx.constraintlayout.compose.ConstraintLayout
import com.babestudios.companyinfouk.companies.R
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
			.wrapContentHeight(Alignment.CenterVertically)
			.clickable { onItemClicked(item) },
	) {
		val (title, description, addressSnippet, companyStatus) = createRefs()

		Text(
			textAlign = TextAlign.Start,
			modifier = modifier
				.padding(start = viewMarginLarge, top = viewMarginNormal)
				.constrainAs(title) {
					top.linkTo(parent.top)
					bottom.linkTo(description.top)
					start.linkTo(parent.start)
				},
			text = item.title ?: "",
			style = CompaniesTypography.titleMedium
		)
		Text(
			modifier = modifier
				.padding(end = viewMarginLarge, top = viewMarginNormal)
				.constrainAs(description) {
					top.linkTo(title.bottom)
					linkTo(parent.start, parent.end, viewMarginLarge, viewMarginLarge, bias = 0f)
				},
			text = item.description ?:"",
			style = CompaniesTypography.bodyMedium
		)
		Text(
			modifier = modifier
				.padding(top = viewMarginNormal, start = viewMarginLarge, bottom = viewMarginNormal) //End fixes bug?? with linkTo
				.constrainAs(addressSnippet) {
					top.linkTo(description.bottom)
					bottom.linkTo(parent.bottom)
					start.linkTo(parent.start)
				},
			text = item.addressSnippet ?:"",
			style = CompaniesTypography.bodyMedium,
			maxLines = 3,
		)
		Text(
			modifier = modifier
				.wrapContentHeight(Alignment.CenterVertically)
				.padding(end = viewMarginLarge, bottom = viewMarginNormal, top = viewMarginNormal)
				.constrainAs(companyStatus) {
					top.linkTo(parent.top)
					bottom.linkTo(parent.bottom)
					end.linkTo(parent.end)
				},
			text = item.companyStatus ?:"",
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
