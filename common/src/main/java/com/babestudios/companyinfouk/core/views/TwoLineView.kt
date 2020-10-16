package com.babestudios.companyinfouk.core.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.common.databinding.ViewTwoLineBinding

/**
 * A two line view with two TextViews aligned vertically
 *
 * The first TextView is single line, the second can be multiline
 *
 * Usually for list items the first TextView should be larger and bold,
 * while for data items should be the opposite (but you can style them any way you like):
 * https://material.io/components/lists#theming
 * https://material.io/components/data-tables#theming
 *
 * Not intended to be used in Lists, only in static lists with a limited number of items
 *
 * TODO Apply textStyle changes
 */

@Suppress("unused")
class TwoLineView @JvmOverloads constructor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

	private var _binding: ViewTwoLineBinding? = null
	private val binding get() = _binding!!

	init {
		_binding = ViewTwoLineBinding.inflate(LayoutInflater.from(context), this)
		obtainStyleAttributes(context, attrs)
	}

	private fun obtainStyleAttributes(context: Context, attrs: AttributeSet?) {
		context.theme.obtainStyledAttributes(attrs, R.styleable.TwoLineView, 0, 0).apply {
			binding.lblTwoLineViewFirstLine.text = getString(R.styleable.TwoLineView_textFirst)
			val textSizeFirst = getDimensionPixelSize(R.styleable.TwoLineView_textSizeFirst, 0)
			if (textSizeFirst > 0) binding.lblTwoLineViewFirstLine.setTextSize(COMPLEX_UNIT_PX, textSizeFirst.toFloat())
			binding.lblTwoLineViewSecondLine.text = getString(R.styleable.TwoLineView_textSecond)
			val textSizeSecond = getDimensionPixelSize(R.styleable.TwoLineView_textSizeSecond, 0)
			if (textSizeSecond > 0) binding.lblTwoLineViewSecondLine.setTextSize(COMPLEX_UNIT_PX, textSizeSecond.toFloat())
			val textSecondMaxLines = getInt(R.styleable.TwoLineView_textSecondMaxLines, 2)
			if (textSecondMaxLines > 0) binding.lblTwoLineViewSecondLine.maxLines = textSecondMaxLines
		}.recycle()
	}

	fun setTextSizeFirst(size: Int) {
		binding.lblTwoLineViewFirstLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
	}

	fun setTextFirst(text: String?) {
		binding.lblTwoLineViewFirstLine.text = text
	}

	fun setTextSizeSecond(size: Int) {
		binding.lblTwoLineViewSecondLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
	}

	fun setTextSecond(text: String?) {
		binding.lblTwoLineViewSecondLine.text = text
	}

	fun setMaxLines(maxLines: Int) {
		binding.lblTwoLineViewSecondLine.maxLines = maxLines
	}
}
