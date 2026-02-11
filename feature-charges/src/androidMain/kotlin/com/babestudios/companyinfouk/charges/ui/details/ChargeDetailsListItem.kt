package com.babestudios.companyinfouk.charges.ui.details

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
import com.babestudios.companyinfouk.design.component.TitleSmallText
import com.babestudios.companyinfouk.shared.domain.model.charges.Transaction

@Composable
internal fun TransactionListItem(
	item: Transaction,
) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Column(
		modifier = Modifier
			.fillMaxWidth(1f)
	) {
		Spacer(modifier = Modifier.height(viewMarginNormal))

		TitleSmallText(
			text = AnnotatedString(item.filingType),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginLarge),
			maxLines = 1,
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		BodyMediumText(
			text = AnnotatedString(item.deliveredOn),
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
private fun TransactionListPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			TransactionListItem(
				Transaction(
					deliveredOn = "2012-03-12",
					filingType = "Registration of a charge (MR01)"
				),
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TransactionListDarkPreview() {
	CompaniesTheme {
		Box(Modifier.background(color = Colors.background)) {
			TransactionListItem(
				Transaction(
					deliveredOn = "2012-03-12",
					filingType = "Registration of a charge (MR01)"
				),
			)
		}
	}
}
