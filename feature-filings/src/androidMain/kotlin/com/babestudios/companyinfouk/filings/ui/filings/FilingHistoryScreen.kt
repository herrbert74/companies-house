package com.babestudios.companyinfouk.filings.ui.filings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.CollapsingToolbarScaffold
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComp
import kotlin.math.min
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FilingHistoryScreen(
	component: FilingHistoryComp,
	modifier: Modifier = Modifier,
) {
	val model by component.state.subscribeAsState()
	val categories = stringArrayResource(R.array.filing_history_categories)
	val bodyContent = rememberSaveable { mutableStateOf(categories.first()) }

	BackHandler(onBack = { component.onBackClicked() })

	CollapsingToolbarScaffold(
		backgroundDrawable = R.drawable.bg_filing_history,
		title = stringResource(com.babestudios.companyinfouk.common.R.string.filing_history),
		onBackClick = { component.onBackClicked() },
		actions = { progress ->
			Text(
				bodyContent.value,
				style = CompaniesTypography.titleSmall.merge(
					TextStyle(
						color = Color(
							min(1f, Colors.onSurface.red + 1 - progress),
							min(1f, Colors.onSurface.green + 1 - progress),
							min(1f, Colors.onSurface.blue + 1 - progress)
						)
					)
				)
			)
			CategoryFilterDropdown(
				categories = categories.toImmutableList(),
				component,
				progress
			)
		},
	) { paddingValues ->
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = modifier.fillMaxSize().padding(paddingValues)
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(modifier.background(color = Color.Red).padding(paddingValues))
		} else {
			FilingHistoryList(
				items = model.filingHistory.items.toImmutableList(),
				paddingValues = paddingValues,
				onItemClick = component::onItemClicked,
				onLoadMore = component::onLoadMore,
			)
		}
	}

}

@Composable
private fun FilingHistoryList(
	items: ImmutableList<FilingHistoryItem>,
	paddingValues: PaddingValues,
	onItemClick: (id: FilingHistoryItem) -> Unit,
	onLoadMore: () -> Unit,
) {
	Box(modifier = Modifier.padding(paddingValues)) {
		val listState = rememberLazyListState()

		LazyColumn(
			Modifier.simpleVerticalScrollbar(listState),
			state = listState
		) {
			items(
				items = items,
				key = { item -> item.hashCode() }
			) { filingHistoryItem ->
				FilingHistoryItemListItem(
					item = filingHistoryItem,
					onItemClick = onItemClick,
				)

				HorizontalDivider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}
