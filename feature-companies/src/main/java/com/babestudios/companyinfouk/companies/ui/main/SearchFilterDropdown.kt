package com.babestudios.companyinfouk.companies.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.babestudios.companyinfouk.domain.model.search.FilterState

@Composable
fun SearchFilterDropdown(
	bodyContent: MutableState<String>,
	searchFilterOptions: Array<String>,
	setFilterState: (FilterState) -> Unit,
) {

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
				tint = MaterialTheme.colorScheme.onPrimaryContainer
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
		searchFilterOptions.forEachIndexed { index, filterOption ->
			DropdownMenuItem(
				text = { Text(filterOption) },
				onClick = {
					selectedIndex = index
					expanded.value = false
					bodyContent.value = filterOption
					setFilterState.invoke(FilterState.values()[index])
				})
		}
	}
}