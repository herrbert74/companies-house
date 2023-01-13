@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.companies.ui.favourites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.domain.model.search.SearchHistoryItem

@Composable
fun FavouritesScreen(component: FavouritesComp) {

	val model by component.state.subscribeAsState()
	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_company,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.favourites)
	) {
		if(model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
			)
		} else {
			FavouritesList(
				items = model.favourites,
				onItemClicked = component::onItemClicked,
			)
		}
	}

}

@Composable
private fun FavouritesList(
	items: List<FavouritesItem>,
	onItemClicked: (id: FavouritesItem) -> Unit,
) {
	Box {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			itemsIndexed(items) { _, favouritesItem ->
				TwoLineCard(
					firstLineString = favouritesItem.searchHistoryItem.companyName,
					secondLineString = favouritesItem.searchHistoryItem.companyNumber,
					flipLineStyles = true
				)

				Divider()
			}
		}

	}
}

@Preview("favourites List Preview")
@Composable
fun FavouritesListPreview() {
	FavouritesList(
		items = listOf(
			FavouritesItem(
				SearchHistoryItem("Reach Plc", "0082548", 123),
				false
			)
		),
		onItemClicked = {},
	)
}
