package com.babestudios.companyinfouk.persons.ui.persons

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
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.common.Address
import com.babestudios.companyinfouk.shared.domain.model.persons.Person

@Composable
internal fun PersonsListItem(
	item: Person,
	onItemClick: (id: Person) -> Unit,
) {

	val viewMarginNormal = Dimens.marginNormal

	Column(
		modifier = Modifier
			.fillMaxWidth(1f)
			.clickable { onItemClick(item) }
	) {

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.name),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginNormal),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.titleSmall.merge(Colors.onBackground)
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))

		Text(
			text = AnnotatedString(item.kind),
			modifier = Modifier
				.align(Alignment.Start)
				.padding(start = viewMarginNormal),
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = CompaniesTypography.bodyMedium.merge(Colors.onBackground)
		)

		Spacer(modifier = Modifier.height(viewMarginNormal))
	}
}

@Preview("Item Preview")
@Composable
private fun DefaultPreview() {
	PersonsListItem(
		Person(
			name = "Robert Who",
			notifiedOn = "",
			address = Address(),
			kind = "Individual",
			naturesOfControl = listOf("individual")
		),
		onItemClick = {}
	)
}
