package com.babestudios.companyinfouk.core.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.View
import android.widget.LinearLayout
import com.babestudios.companyinfouk.common.R
import kotlinx.android.synthetic.main.view_two_line.view.*

/**
 * A two line view with two TextViews aligned vertically
 *
 * The first TextView is single line, the second can be multiline
 *
 * Usually for list items the first TextView should be larger and bold,
 * while for data items is should be the opposite (but you can style them any way you like):
 * https://material.io/components/lists#theming
 * https://material.io/components/data-tables#theming
 *
 * TODO Apply textStyle changes
 */

@Suppress("unused")
class TwoLineView @JvmOverloads constructor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

	init {
		View.inflate(context, R.layout.view_two_line, this)
		obtainStyleAttributes(context, attrs)
	}

	private fun obtainStyleAttributes(context: Context, attrs: AttributeSet?) {
		context.theme.obtainStyledAttributes(attrs, R.styleable.TwoLineView, 0, 0).apply {
			lblTwoLineViewFirstLine.text = getString(R.styleable.TwoLineView_textFirst)
			val textSizeFirst = getDimensionPixelSize(R.styleable.TwoLineView_textSizeFirst, 0)
			if (textSizeFirst > 0) lblTwoLineViewFirstLine.setTextSize(COMPLEX_UNIT_PX, textSizeFirst.toFloat())
			lblTwoLineViewSecondLine.text = getString(R.styleable.TwoLineView_textSecond)
			val textSizeSecond = getDimensionPixelSize(R.styleable.TwoLineView_textSizeSecond, 0)
			if (textSizeSecond > 0) lblTwoLineViewSecondLine.setTextSize(COMPLEX_UNIT_PX, textSizeSecond.toFloat())
		}.recycle()
	}

	fun setTextSizeFirst(size: Int) {
		lblTwoLineViewFirstLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
	}

	fun setTextFirst(text: String) {
		lblTwoLineViewFirstLine.text = text
	}

	fun setTextSizeSecond(size: Int) {
		lblTwoLineViewSecondLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
	}

	fun setTextSecond(text: String) {
		lblTwoLineViewSecondLine.text = text
	}
}
