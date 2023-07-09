package com.babestudios.companyinfouk.common.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.shared.domain.FORTY_PERCENT

@Composable
fun LabeledDetailCardItem(
	labelString: String,
	detailString: CharSequence,
	modifier: Modifier = Modifier,
	labelStyle: TextStyle = CompaniesTypography.bodyMedium,
	detailStyle: TextStyle = CompaniesTypography.titleLargeBold,
	labelWeight: Float = FORTY_PERCENT,
) {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Row(
		horizontalArrangement = Arrangement.Center,
		modifier = modifier
	) {
		Text(
			textAlign = TextAlign.Start,
			modifier = Modifier
				.padding(start = viewMarginLarge, top = viewMarginNormal, bottom = viewMarginNormal)
				.weight(labelWeight)
				.alignByBaseline(),
			text = labelString,
			style = labelStyle,
		)
		if (detailString is AnnotatedString) {
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
					.weight(1 - labelWeight)
					.alignByBaseline(),
				text = detailString,
				style = detailStyle,
			)
		} else {
			Text(
				modifier = Modifier
					.padding(horizontal = viewMarginLarge, vertical = viewMarginNormal)
					.weight(1 - labelWeight)
					.alignByBaseline(),
				text = detailString.toString(),
				style = detailStyle,
			)
		}
	}
}

//Annotated preview in FilingDetailsScreen

@Preview("Normal")
@Composable
fun NormalPreview() {
	LabeledDetailCardItem(
		labelString = "Date of birth",
		detailString = "1974-09-01",
	)
}

@Preview("Long label")
@Composable
fun LongLabelPreview() {
	LabeledDetailCardItem(
		labelString = "Date of birth, but very long",
		detailString = "1974-09-01",
	)
}

@Preview("Long detail")
@Composable
fun LongDetailPreview() {
	LabeledDetailCardItem(
		labelString = "Name",
		detailString = "Longyearbien Longlongyear",
	)
}
