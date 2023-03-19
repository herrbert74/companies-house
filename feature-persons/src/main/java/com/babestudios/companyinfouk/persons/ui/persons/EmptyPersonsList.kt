package com.babestudios.companyinfouk.persons.ui.persons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.persons.R

@Composable
fun EmptyPersonsList() {

	val viewMarginLarge = dimensionResource(com.babestudios.base.R.dimen.viewMarginLarge)

	Column(
		Modifier
			.fillMaxSize(1f)
			//Matches the empty icon background from BaBeStudiosBase
			.background(colorResource(com.babestudios.base.R.color.grey_1)),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(com.babestudios.base.R.drawable.ic_business_empty),
			contentDescription = null
		)
		Text(
			text = stringResource(R.string.no_person_with_significant_control),
			style = CompaniesTypography.titleLargeBold,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.padding(all = viewMarginLarge * 2)
		)
	}
}

@Preview("Empty Favourites List Preview")
@Composable
fun EmptyFavouritesListPreview() {
	EmptyPersonsList()
}
