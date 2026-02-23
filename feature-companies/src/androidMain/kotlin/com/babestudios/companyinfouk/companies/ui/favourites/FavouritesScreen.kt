package com.babestudios.companyinfouk.companies.ui.favourites

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesComp
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesItem
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore
import com.babestudios.companyinfouk.shared.screen.favourites.FavouritesStore.SideEffect.Initial
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavouritesScreen(
	component: FavouritesComp,
	modifier: Modifier = Modifier,
) {
	val model by component.state.subscribeAsState()

	val sideEffect by component.sideEffects.collectAsState(Initial)

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_favourites,
		title = stringResource(R.string.favourites),
		onBackClick = { component.onBackClicked() },
	) { paddingValues ->
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = modifier
					.fillMaxSize()
					.padding(paddingValues)
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
					.padding(paddingValues)
			)
		} else {
			FavouritesList(
				items = model.favourites.toImmutableList(),
				paddingValues = paddingValues,
				onItemClick = component::onItemClicked,
				onDeleteClick = component::onDeleteClicked,
				onUndoClick = component::onUndoDeleteClicked,
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
	items: ImmutableList<FavouritesItem>,
	paddingValues: PaddingValues,
	onItemClick: (favouritesItem: FavouritesItem) -> Unit,
	onDeleteClick: (favouritesItem: FavouritesItem) -> Unit,
	onUndoClick: (favouritesItem: FavouritesItem) -> Unit,
) {
	val viewMarginLarge = Dimens.marginLarge

	Box(
		modifier = Modifier
			.padding(paddingValues)
	) {
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
									.clickable { onItemClick(favouritesItem) }
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
										.clickable { onUndoClick(favouritesItem) }
								)
							} else {
								IconButton(
									onClick = { onDeleteClick(favouritesItem) },
									modifier = Modifier.padding(
										end = viewMarginLarge
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
private fun FavouritesListPreview() {
	FavouritesList(
		items = persistentListOf(
			FavouritesItem(
				SearchHistoryItem("Reach Plc", "0082548", 1),
				false
			),
			FavouritesItem(
				SearchHistoryItem("Reach Plc", "0082548", 1),
				true
			)
		),
		paddingValues = PaddingValues(0.dp),
		onItemClick = {},
		onDeleteClick = {},
		onUndoClick = {},
	)
}
