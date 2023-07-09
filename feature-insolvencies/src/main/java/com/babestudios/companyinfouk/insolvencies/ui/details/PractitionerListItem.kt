package com.babestudios.companyinfouk.insolvencies.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.insolvency.Practitioner
import com.babestudios.companyinfouk.insolvencies.R

@Composable
internal fun PractitionerListItem(
	item: Practitioner,
	onItemClicked: (practitioner: Practitioner) -> Unit,
) {
	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Row(
		modifier = Modifier
			.fillMaxWidth(1f)
			.clickable { onItemClicked(item) },
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Image(
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
			alignment = Alignment.CenterEnd,
			ContentScale.FillBounds,
		)
		Text(
			text = AnnotatedString(item.name),
			modifier = Modifier
				.align(Alignment.CenterVertically),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleMedium
		)

	}
}

@Preview("Item Preview")
@Composable
fun DefaultPreview() {
	PractitionerListItem(
		Practitioner(
			name = "John Doe",
			address = Address(),
			appointedOn = "2016-02-26",
			ceasedToActOn = "2017-02-26",
			role = "practitioner"
		),
		onItemClicked = {}
	)
}
