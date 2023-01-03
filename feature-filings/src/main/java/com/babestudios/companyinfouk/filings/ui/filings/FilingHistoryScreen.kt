@file:Suppress("UNUSED_PARAMETER, FunctionNaming")

package com.babestudios.companyinfouk.filings.ui.filings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.babestudios.companyinfouk.common.compose.HeaderCollapsingToolbarScaffold
import com.babestudios.companyinfouk.common.compose.InfiniteListHandler
import com.babestudios.companyinfouk.common.compose.simpleVerticalScrollbar
import com.babestudios.companyinfouk.design.CompaniesTypography
import com.babestudios.companyinfouk.domain.model.filinghistory.FilingHistoryItem
import com.babestudios.companyinfouk.filings.R

@Composable
fun FilingHistoryScreen(component: FilingHistoryComp) {

	val model by component.state.subscribeAsState()
	val categories = stringArrayResource(R.array.filing_history_categories)
	val bodyContent = rememberSaveable { mutableStateOf(categories.first()) }

	HeaderCollapsingToolbarScaffold(
		headerBackgroundResource = R.drawable.bg_filing_history,
		navigationAction = { component.onBackClicked() },
		topAppBarActions = {
			Text(
				bodyContent.value,
				style = CompaniesTypography.titleSmall.merge(TextStyle(color = MaterialTheme.colorScheme.onPrimary))
			)
			CategoryFilterDropdown(categories = categories.toList(), bodyContent = bodyContent, component)
		},
		title = stringResource(R.string.filing_history)
	) {
		if (model.isLoading) {
			CircularProgressIndicator()
		} else if (model.error != null) {
			Box(
				Modifier
					.background(color = Color.Red)
			)
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
			itemsIndexed(items) { _, FilingHistoryItem ->
				FilingHistoryItemListItem(
					item = FilingHistoryItem,
					onItemClicked = onItemClicked,
				)

				Divider()
			}
		}

		InfiniteListHandler(listState = listState) {
			onLoadMore()
		}

	}

}

@Composable
fun CategoryFilterDropdown(categories: List<String>, bodyContent: MutableState<String>, component: FilingHistoryComp) {
	val expanded = remember { mutableStateOf(false) }
	var selectedIndex by remember { mutableStateOf(0) }

	Box(
		Modifier
			.wrapContentSize(Alignment.TopEnd)
	) {
		IconButton(onClick = {
			expanded.value = true
		}) {
			Icon(
				Icons.Filled.ArrowDropDown,
				contentDescription = "More Menu",
				tint = MaterialTheme.colorScheme.onPrimary
			)
		}
	}

	DropdownMenu(
		expanded = expanded.value,
		onDismissRequest = { expanded.value = false },
		modifier = Modifier
			.wrapContentWidth()
			.background(Color.LightGray)
	) {
		categories.forEachIndexed { index, category ->
			DropdownMenuItem(
				text = { Text(category) },
				onClick = {
					selectedIndex = index
					expanded.value = false
					bodyContent.value = category
					component.onFilingHistoryCategorySelected(index)
				})
		}
	}
}
