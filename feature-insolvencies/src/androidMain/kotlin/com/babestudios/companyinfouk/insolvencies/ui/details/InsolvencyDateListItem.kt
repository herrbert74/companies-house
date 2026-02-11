package com.babestudios.companyinfouk.insolvencies.ui.details

import android.content.res.Configuration
import androidx.compose.foundation.background
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

@Composable
internal fun InsolvencyDateListItem(item: Date) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Column(
		modifier = Modifier
			.fillMaxWidth(1f)
	) {
		Spacer(modifier = Modifier.height(viewMarginNormal))

		TitleMediumText(
			text = AnnotatedString(item.date),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		BodyMediumText(
			text = AnnotatedString(item.type),
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
private fun InsolvencyDateItemPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			InsolvencyDateListItem(
				Date(
					"2016-05-26",
					"Commencement of winding up"
				)
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun InsolvenciesDateItemDarkPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			InsolvencyDateListItem(
				Date(
					"2016-05-26",
					"Commencement of winding up"
				)
			)
		}
	}
}
