package com.babestudios.companyinfouk.common.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

const val SCROLL_IN_PROGRESS_DURATION = 150
const val SCROLL_NOT_IN_PROGRESS_DURATION = 500

/**
 *
 * A simple vertical scrollbar solution until Google officially adds it
 *
 * Copied from:
 * https://stackoverflow.com/questions/66341823/jetpack-compose-scrollbars/68056586#68056586
 *
 * Google Issue tracker:
 * https://issuetracker.google.com/issues/173001017
 *
 */
fun Modifier.simpleVerticalScrollbar(
	state: LazyListState,
	width: Dp = 8.dp,
): Modifier = composed {
	val targetAlpha = if (state.isScrollInProgress) 1f else 0f
	val duration = if (state.isScrollInProgress) SCROLL_IN_PROGRESS_DURATION else SCROLL_NOT_IN_PROGRESS_DURATION

	val alpha by animateFloatAsState(
		targetValue = targetAlpha,
		animationSpec = tween(durationMillis = duration)
	)

	drawWithContent {
		drawContent()

		val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
		val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

		// Draw scrollbar if scrolling or if the animation is still running and lazy column has content
		if (needDrawScrollbar && firstVisibleElementIndex != null) {
			val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
			val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
			val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

			drawRect(
				color = Color.LightGray,
				topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
				size = Size(width.toPx(), scrollbarHeight),
				alpha = alpha
			)
		}
	}
}
