package com.babestudios.companyinfouk.companies.ui.favourites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesComp
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.SideEffect.Initial

@Composable
fun FavouritesScreen(component: FavouritesComp) {

	val model by component.state.subscribeAsState()

	val sideEffect by component.sideEffects.collectAsState(Initial)

	BackHandler(onBack = { component.onBackClicked() })

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_favourites,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {},
		title = stringResource(R.string.favourites)
	) {
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxSize()
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
			)
		} else {
			FavouritesList(
				items = model.favourites,
				onItemClicked = component::onItemClicked,
				onDeleteClicked = component::onDeleteClicked,
				onUndoClicked = component::onUndoDeleteClicked,
			)
		}
		if (sideEffect == FavouritesStore.SideEffect.Back) {
			println("zsoltbertalan* Scaffold: show side effect")
			component.goBack()
		}
	}

}

@Composable
private fun FavouritesList(
	items: List<FavouritesItem>,
	onItemClicked: (favouritesItem: FavouritesItem) -> Unit,
	onDeleteClicked: (favouritesItem: FavouritesItem) -> Unit,
	onUndoClicked: (favouritesItem: FavouritesItem) -> Unit,
) {

	val viewMarginLarge = Dimens.marginLarge

	Box {
		val listState = rememberLazyListState()

		if (items.isEmpty()) {
			EmptyFavouritesList()
		} else {
			LazyColumn(
				state = listState,
			) {
				items(
					items = items,
					key = { item -> item.hashCode() },
					itemContent = { favouritesItem ->
						Row(
							Modifier.fillMaxWidth(1f),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.Start
						) {
							TwoLineCard(
								modifier = Modifier
									.clickable { onItemClicked(favouritesItem) }
									.weight(1f),
								firstLineString = favouritesItem.searchHistoryItem.companyName,
								secondLineString = favouritesItem.searchHistoryItem.companyNumber,
								flipLineStyles = true
							)
							if (favouritesItem.isPendingRemoval) {
								Text(
									text = "UNDO",
									modifier = Modifier
										.padding(end = viewMarginLarge)
										.clickable { onUndoClicked(favouritesItem) }
								)
							} else {
								IconButton(
									onClick = { onDeleteClicked(favouritesItem) }, modifier = Modifier.padding(
										end =
										viewMarginLarge
									)
								) {
									Icon(
										Icons.Filled.Delete,
										contentDescription = "Delete Favourite",
									)
								}
							}
						}

						HorizontalDivider()
					}
				)
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
				SearchHistoryItem("Reach Plc", "0082548", 1),
				false
			),
			FavouritesItem(
				SearchHistoryItem("Reach Plc", "0082548", 1),
				true
			)
		),
		onItemClicked = {},
		onDeleteClicked = {},
		onUndoClicked = {},
	)
}
