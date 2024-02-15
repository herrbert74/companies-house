package com.babestudios.companyinfouk.insolvencies.ui.insolvencies

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.BodyMediumText
import com.babestudios.companyinfouk.design.component.TitleMediumText
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Date
import com.babestudios.companyinfouk.shared.domain.model.insolvency.InsolvencyCase

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

		TitleMediumText(
			text = AnnotatedString(item.dates[0].date),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		BodyMediumText(
			text = AnnotatedString(item.type ?: ""),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))
	}
}

@Preview
@Composable
fun InsolvenciesItemPreview() {
	CompaniesTheme{
		Box(modifier = Modifier.background(Colors.background)){
			InsolvenciesListItem(
				InsolvencyCase(
					dates = listOf(Date("2016-02-16", "Creditors voluntary liquidation")),
					type = "Creditors voluntary liquidation"
				),
				onItemClicked = {}
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun InsolvenciesItemDarkPreview() {
	CompaniesTheme{
		Box(modifier = Modifier.background(Colors.background)){
			InsolvenciesListItem(
				InsolvencyCase(
					dates = listOf(Date("2016-02-16", "Creditors voluntary liquidation")),
					type = "Creditors voluntary liquidation"
				),
				onItemClicked = {}
			)
		}
	}
}
