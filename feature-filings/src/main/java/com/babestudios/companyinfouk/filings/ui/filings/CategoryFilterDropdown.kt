package com.babestudios.companyinfouk.filings.ui.filings

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.babestudios.companyinfouk.design.Colors
import com.babestudios.companyinfouk.shared.screen.filings.FilingHistoryComp
import kotlin.math.min

@Composable
fun CategoryFilterDropdown(
	categories: List<String>,
	bodyContent: MutableState<String>,
	component: FilingHistoryComp,
	progress: Float,
) {

	val expanded = remember { mutableStateOf(false) }
	var selectedIndex by remember { mutableIntStateOf(0) }

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
				tint = Color(
					min(1f, Colors.onSurface.red + 1 - progress),
					min(1f, Colors.onSurface.green + 1 - progress),
					min(1f, Colors.onSurface.blue + 1 - progress)
				)
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
