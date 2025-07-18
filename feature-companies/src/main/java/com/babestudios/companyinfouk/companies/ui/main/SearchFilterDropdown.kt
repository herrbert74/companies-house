package com.babestudios.companyinfouk.companies.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.shared.domain.model.search.FilterState

@Composable
fun SearchFilterDropdown(
	searchFilterOptions: Array<String>,
	setFilterState: (FilterState) -> Unit,
	modifier: Modifier = Modifier,
) {

	val expanded = remember { mutableStateOf(false) }

	Column(modifier = modifier) {
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
					tint = Colors.onPrimaryContainer
				)
			}
		}

		DropdownMenu(
			expanded = expanded.value,
			onDismissRequest = { expanded.value = false },
			modifier = Modifier
				.wrapContentWidth()
				.background(Colors.surfaceVariant)
		) {
			searchFilterOptions.forEachIndexed { index, filterOption ->
				DropdownMenuItem(
					text = { Text(filterOption) },
					onClick = {
						expanded.value = false
						setFilterState.invoke(FilterState.entries[index])
					}
				)
			}
		}
	}
}
