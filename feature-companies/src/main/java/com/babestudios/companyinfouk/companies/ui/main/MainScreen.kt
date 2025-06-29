package com.babestudios.companyinfouk.companies.ui.main

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.TwoLineCard
import com.babestudios.companyinfouk.companies.R
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTheme
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.design.Dimens
import com.babestudios.companyinfouk.design.titleLargeBold
import com.babestudios.companyinfouk.design.titleMediumBold
import com.babestudios.companyinfouk.shared.domain.RECENT_SEARCHES
import com.babestudios.companyinfouk.shared.domain.model.search.CompanySearchResultItem
import com.babestudios.companyinfouk.shared.domain.model.search.FilterState
import com.babestudios.companyinfouk.shared.domain.model.search.SearchHistoryItem
import com.babestudios.companyinfouk.shared.screen.main.MainComp

//TODO transparent search when there are no results
@Composable
fun MainScreen(component: MainComp) {

	val model by component.state.subscribeAsState()
	var searchQuery by rememberSaveable { mutableStateOf("") }
	var isSearchBarActive by rememberSaveable { mutableStateOf(false) }
	var showMenu by remember { mutableStateOf(false) }
	val isClearRecentsDialogVisible = remember { mutableStateOf(false) }
	val focusManager = LocalFocusManager.current

	val searchFilterOptions = stringArrayResource(R.array.search_filter_options)
	val searchFilterString = rememberSaveable { mutableStateOf(searchFilterOptions.first()) }

	val topAppBarColors = TopAppBarDefaults.topAppBarColors(
		containerColor = Colors.surface,
		scrolledContainerColor = Colors.surfaceContainer,
	)

	/**
	 * We need to set a separate colour for SearchBar and its content to allow semi transparent content,
	but it's not currently possible.'
	 */
//	val searchBarColors = SearchBarDefaults.colors(
//		containerColor = colorResource(R.color.semiTransparentBlack),
//	)

	fun closeSearchBar() {
		focusManager.clearFocus()
		isSearchBarActive = false
	}

	Scaffold(
		topBar = {
			if (!isSearchBarActive) {
				TopAppBar(
					colors = topAppBarColors,
					title = { Text("Company Info UK") },
					actions = {
						IconButton(onClick = {
							isSearchBarActive = !isSearchBarActive
							if (isSearchBarActive) component.setSearchMenuItemExpanded()
							else component.setSearchMenuItemCollapsed()
						}) {
							Icon(
								painter = painterResource(R.drawable.ic_search),
								contentDescription = "Search icon",
								tint = Colors.onPrimaryContainer
							)
						}
						IconButton(onClick = { component.onFavoritesClicked() }) {
							Icon(
								painter = painterResource(R.drawable.ic_favorite),
								contentDescription = "Favourites",
								tint = Colors.onPrimaryContainer
							)
						}
						IconButton(onClick = { showMenu = !showMenu }) {
							Icon(
								Icons.Default.MoreVert,
								contentDescription = stringResource(R.string.privacy_policy),
								tint = Colors.onPrimaryContainer
							)
						}
						DropdownMenu(
							expanded = showMenu,
							onDismissRequest = { showMenu = false }
						) {
							DropdownMenuItem(
								onClick = { component.onPrivacyClicked() },
								text = { Text(stringResource(R.string.privacy_policy)) }
							)
						}
					},
				)
			}
		}
	) { paddingValues ->

		if (model.searchHistoryItems.isEmpty()) {
			EmptySearchList(stringResource(R.string.no_recent_searches))
		} else {
			RecentSearchesList(
				paddingValues = paddingValues,
				items = model.searchHistoryItems,
				isClearRecentsDialogVisible = isClearRecentsDialogVisible,
				onItemClicked = component::onRecentSearchesItemClicked,
			)
			if (isClearRecentsDialogVisible.value) {
				ClearRecentsDialog(
					isClearRecentsDialogVisible = isClearRecentsDialogVisible,
					onClearRecentSearchesClicked = component::onClearRecentSearchesClicked,
				)
			}
		}
		if (isSearchBarActive) {
			SearchBar(
				modifier = Modifier.fillMaxWidth(),
				inputField = {
					SearchBarDefaults.InputField(
						query = searchQuery,
						onQueryChange = {
							searchQuery = it
							component.onSearchQueryChanged(it)
						},
						onSearch = { closeSearchBar() },
						enabled = true,
						onExpandedChange = {
							isSearchBarActive = it
							if (!isSearchBarActive) focusManager.clearFocus()
						},
						expanded = isSearchBarActive,
						//colors = searchBarColors,
						placeholder = { Text(stringResource(R.string.search_prompt)) },
						leadingIcon = {
							Icon(
								modifier = Modifier.clickable {
									isSearchBarActive = false
									searchQuery = ""
									searchFilterString.value = searchFilterOptions.first()
									component.onSearchQueryChanged(null)
									component.setFilterState(FilterState.FILTER_SHOW_ALL)
								},
								imageVector = Icons.AutoMirrored.Filled.ArrowBack,
								contentDescription = null
							)
						},
						trailingIcon = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Text(
									searchFilterString.value,
									style = CompaniesTypography.titleSmall.merge(
										TextStyle(color = Colors.onPrimaryContainer)
									)
								)
								SearchFilterDropdown(
									bodyContent = searchFilterString,
									searchFilterOptions = searchFilterOptions,
									setFilterState = component::setFilterState
								)
							}
						},
					)
				},
				expanded = isSearchBarActive,
				onExpandedChange = { isSearchBarActive = it },
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
				} else if (searchQuery.length > 2 && model.filteredSearchResultItems.isEmpty()) {
					EmptySearchList()
				} else {
					SearchResultList(
						items = model.filteredSearchResultItems,
						onItemClicked = component::onItemClicked,
						onLoadMore = component::loadMoreSearch,
					)
				}
			}
		}
	}
}

@Composable
private fun MainHeader() {

	val viewMarginLarge = Dimens.marginLarge
	val viewMarginNormal = Dimens.marginNormal

	Text(
		modifier = Modifier.padding(vertical = viewMarginNormal, horizontal = viewMarginLarge),
		text = RECENT_SEARCHES,
		style = CompaniesTypography.titleMediumBold
	)

}

/**
 * Used for both search results and recent searches
 */
@Composable
private fun EmptySearchList(
	message: String = stringResource(R.string.no_search_result),
) {

	val viewMarginLarge = Dimens.marginLarge

	Column(
		Modifier
			.fillMaxSize(1f)
			//Matches the empty icon background from BaBeStudiosBase
			.background(colorResource(com.babestudios.companyinfouk.common.R.color.grey_1)),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Image(
			painter = painterResource(com.babestudios.base.android.R.drawable.ic_business_empty),
			contentDescription = null
		)
		Text(
			text = message,
			style = CompaniesTypography.titleLargeBold,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.align(Alignment.CenterHorizontally)
				.padding(all = viewMarginLarge * 2)
		)
	}
}

@Composable
private fun RecentSearchesList(
	paddingValues: PaddingValues,
	items: List<SearchHistoryItem>,
	isClearRecentsDialogVisible: MutableState<Boolean>,
	onItemClicked: (id: SearchHistoryItem) -> Unit,
) {

	val viewMarginLarge = Dimens.marginLarge

	Box(Modifier.fillMaxSize(1f)) {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier
				.simpleVerticalScrollbar(listState)
				.padding(paddingValues),
			state = listState
		) {
			itemsIndexed(
				items = items,
				key = { _, item -> item.hashCode() }
			) { index, searchHistoryItem ->
				if (index == 0) {
					MainHeader()
				}
				TwoLineCard(
					modifier = Modifier
						.clickable { onItemClicked(searchHistoryItem) }
						.fillMaxWidth(1f),
					firstLineString = searchHistoryItem.companyName,
					secondLineString = searchHistoryItem.companyNumber,
					flipLineStyles = true
				)

				HorizontalDivider()
			}
		}

		LargeFloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(bottom = viewMarginLarge + paddingValues.calculateBottomPadding(), end = viewMarginLarge),
			onClick = {
				isClearRecentsDialogVisible.value = true
			},
		) {
			Icon(
				imageVector = Icons.Filled.Delete,
				contentDescription = "Add favourites",
				tint = Colors.onPrimaryContainer
			)
		}

	}
}

@Composable
fun ClearRecentsDialog(
	isClearRecentsDialogVisible: MutableState<Boolean>,
	onClearRecentSearchesClicked: () -> Unit,
) {
	AlertDialog(
		onDismissRequest = {
			isClearRecentsDialogVisible.value = false
		},
		title = {
			Text(text = stringResource(R.string.delete_recent_searches))
		},
		text = {
			Text(text = stringResource(R.string.are_you_sure_you_want_to_delete_all_recent_searches))
		},
		confirmButton = {
			Button(
				onClick = {
					isClearRecentsDialogVisible.value = false
					onClearRecentSearchesClicked()
				}) {
				Text(stringResource(android.R.string.ok))
			}
		},
		dismissButton = {
			Button(
				onClick = {
					isClearRecentsDialogVisible.value = false
				}) {
				Text(stringResource(android.R.string.cancel))
			}
		}
	)

}

@Composable
private fun SearchResultList(
	items: List<CompanySearchResultItem>,
	onItemClicked: (id: CompanySearchResultItem) -> Unit,
	onLoadMore: () -> Unit,
) {

	val backgroundColor =
		if (items.isEmpty()) colorResource(android.R.color.transparent)//MaterialTheme.colorScheme.background
		else colorResource(android.R.color.transparent)

	Box(
		Modifier
			.fillMaxSize(1f)
			.background(backgroundColor)
	) {

		val listState = rememberLazyListState()

		LazyColumn(
			Modifier
				.simpleVerticalScrollbar(listState)
				.background(backgroundColor),
			state = listState
		) {
			items(
				items = items,
				key = { item -> item.hashCode() },
				itemContent = { companySearchResultItem ->
					CompanySearchResultItemListItem(
						item = companySearchResultItem,
						onItemClicked = onItemClicked,
					)

					HorizontalDivider()
				})
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EmptyRecentListPreview() {
	CompaniesTheme {
		EmptySearchList()
	}
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ClearRecentsDialogPreview() {
	CompaniesTheme {
		ClearRecentsDialog(
			isClearRecentsDialogVisible = remember { mutableStateOf(true) },
			onClearRecentSearchesClicked = {}
		)
	}
}

@Preview(name = "Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@PreviewLightDark
@Composable
fun MainListPreview() {
	CompaniesTheme {
		SearchResultList(
			items = listOf(
				CompanySearchResultItem(
					title = "ALPHABET ACCOUNTANTS LTD",
					description = "07620277 - Incorporated on  3 May 2011",
					addressSnippet = "16 Anchor Street, Chelmsford, CM2 0JY",
					companyStatus = "active"
				),
				CompanySearchResultItem(
					title = "ALPHABET ACCOUNTANTS LTD 2",
					description = "07620278 - Incorporated on  3 May 2011",
					addressSnippet = "16 Anchor Street, Chelmsford, CM2 0JY",
					companyStatus = "active"
				)
			),
			onItemClicked = {},
			onLoadMore = {}
		)
	}
}
