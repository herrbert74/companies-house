package com.babestudios.companyinfouk.companies.ui.favourites

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold

@Composable
fun EmptyFavouritesList(
	modifier: Modifier = Modifier,
) {

	val viewMarginLarge = Dimens.marginLarge

	Column(
		modifier
			.fillMaxSize(1f)
			.background(colorResource(com.babestudios.companyinfouk.common.R.color.grey_1)), //Matches the empty icon
		// background from
		// BaBeStudiosBase
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(com.babestudios.base.android.R.drawable.ic_business_empty_favorites),
			contentDescription = null
		)
		Text(
			text = stringResource(com.babestudios.base.android.R.string.empty_list),
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
private fun EmptyFavouritesListPreview() {
	EmptyFavouritesList()
}
