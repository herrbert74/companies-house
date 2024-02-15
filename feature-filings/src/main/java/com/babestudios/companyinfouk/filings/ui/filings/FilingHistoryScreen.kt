package com.babestudios.companyinfouk.filings.ui.filings

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.base.compose.InfiniteListHandler
import com.babestudios.base.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.filings.R
import com.babestudios.companyinfouk.shared.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComp
import kotlin.math.min
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun FilingHistoryScreen(component: FilingHistoryComp) {

	val model by component.state.subscribeAsState()
	val categories = stringArrayResource(R.array.filing_history_categories)
	val bodyContent = rememberSaveable { mutableStateOf(categories.first()) }

	BackHandler(onBack = { component.onBackClicked() })

	val state = rememberCollapsingToolbarScaffoldState()

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_filing_history,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {
			Text(
				bodyContent.value,
				style = CompaniesTypography.titleSmall.merge(
					TextStyle(
						color = Color(
							min(1f, Colors.onSurface.red + state.toolbarState.progress),
							min(1f, Colors.onSurface.green + state.toolbarState.progress),
							min(1f, Colors.onSurface.blue + state.toolbarState.progress)
						)
					)
				)
			)
			CategoryFilterDropdown(categories = categories.toList(), bodyContent = bodyContent, component, state)
		},
		title = stringResource(com.babestudios.companyinfouk.common.R.string.filing_history),
		state
	) {
		if (model.isLoading) {
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxSize()
			) {
				CircularProgressIndicator()
			}
		} else if (model.error != null) {
			Box(Modifier.background(color = Color.Red))
		} else {
			FilingHistoryList(
				items = model.filingHistory.items,
				onItemClicked = component::onItemClicked,
				onLoadMore = component::onLoadMore,
			)
		}
	}

}

@Composable
private fun FilingHistoryList(
	items: List<FilingHistoryItem>,
	onItemClicked: (id: FilingHistoryItem) -> Unit,
	onLoadMore: () -> Unit,
) {

	Box {

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
					onItemClicked = onItemClicked,
				)

				HorizontalDivider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}
