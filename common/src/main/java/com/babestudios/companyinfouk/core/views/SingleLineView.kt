package com.babestudios.companyinfouk.core.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue.COMPLEX_UNIT_PX
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.babestudios.companyinfouk.common.R
import com.babestudios.companyinfouk.common.databinding.ViewSingleLineBinding

/**
 * A single line view with an ImageView and a TextView aligned horizontally
 *
 * You have to add focusable and clickable if you want to make the whole view clickable
 */

class SingleLineView @JvmOverloads constructor(
		context: Context,
		attrs: AttributeSet? = null,
		defStyleAttr: Int = R.attr.singleLineStyle
) : LinearLayout(context, attrs, defStyleAttr) {

	private var _binding: ViewSingleLineBinding? = null
	private val binding get() = _binding!!

	init {
		_binding = ViewSingleLineBinding.inflate(LayoutInflater.from(context), this)
		obtainStyleAttributes(context, attrs, defStyleAttr)
	}

	private fun obtainStyleAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
		context.theme.obtainStyledAttributes(attrs, R.styleable.SingleLineView, defStyleAttr, 0).apply {
			binding.ivSingleLine.setImageResource(getResourceId(R.styleable.SingleLineView_imageViewSrc, 0))
			val contentDescription = getString(R.styleable.SingleLineView_imageViewContentDescription)
			contentDescription?.let { binding.ivSingleLine.contentDescription = it }
			binding.lblSingleLine.text = getString(R.styleable.SingleLineView_text)
			val textSize = getDimensionPixelSize(R.styleable.SingleLineView_textSize, 0)
			if (textSize > 0) binding.lblSingleLine.setTextSize(COMPLEX_UNIT_PX, textSize.toFloat())
		}.recycle()
	}
}
