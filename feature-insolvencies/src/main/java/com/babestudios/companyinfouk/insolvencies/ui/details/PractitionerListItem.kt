package com.babestudios.companyinfouk.insolvencies.ui.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.component.TitleMediumText
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R

@Composable
internal fun PractitionerListItem(
	item: Practitioner,
	onItemClick: (practitioner: Practitioner) -> Unit,
) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Row(
		modifier = Modifier
			.fillMaxWidth(1f)
			.clickable { onItemClick(item) },
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Icon(
			painter = painterResource(R.drawable.ic_male_lawyer),
			contentDescription = "status",
			modifier = Modifier
				.padding(
					start = viewMarginLarge,
					top = viewMarginNormal,
					bottom = viewMarginNormal,
					end = viewMarginLarge
				)
				.height(Dimens.listAvatarWidth)
				.width(Dimens.listAvatarWidth),
			tint = Colors.onBackground
		)
		TitleMediumText(
			text = AnnotatedString(item.name),
			modifier = Modifier
				.align(Alignment.CenterVertically),
			maxLines = 1,
		)

	}
}

@Preview
@Composable
private fun PractitionerListItemPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			PractitionerListItem(
				Practitioner(
					name = "John Doe",
					address = Address(),
					appointedOn = "2016-02-26",
					ceasedToActOn = "2017-02-26",
					role = "practitioner"
				),
				onItemClick = {}
			)
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PractitionerListItemDarkPreview() {
	CompaniesTheme {
		Box(modifier = Modifier.background(Colors.background)) {
			PractitionerListItem(
				Practitioner(
					name = "John Doe",
					address = Address(),
					appointedOn = "2016-02-26",
					ceasedToActOn = "2017-02-26",
					role = "practitioner"
				),
				onItemClick = {}
			)
		}
	}
}
