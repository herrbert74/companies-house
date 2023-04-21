package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.domain.model.insolvency.Date
import com.babestudios.companyinfouk.domain.model.insolvency.InsolvencyCase

@Composable
internal fun InsolvenciesListItem(
	item: InsolvencyCase,
	onItemClicked: (id: InsolvencyCase) -> Unit,
) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Column(
		modifier = Modifier
			.fillMaxWidth(1f)
			.clickable { onItemClicked(item) }
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.dates[0].date),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.type ?: ""),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyMedium
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))
	}
}

@Preview("Item Preview")
@Composable
fun DefaultPreview() {
	InsolvenciesListItem(
		InsolvencyCase(
			dates = listOf(Date("2016-02-16", "Creditors voluntary liquidation")),
			type = "Creditors voluntary liquidation"
		),
		onItemClicked = {}
	)
}
