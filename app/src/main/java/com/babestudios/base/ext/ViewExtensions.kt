package com.babestudios.base.ext

import android.content.Context
import android.view.View
import android.widget.TextView

val View.ctx: Context
	get() = context

var TextView.textColor: Int
	get() = currentTextColor
	set(v) = setTextColor(v)

private inline fun <T> TextView.updateOrHide(content: T?, action: TextView.(content: T) -> Unit) {
	if (content != null) {
		visibility = View.VISIBLE
		action(content)
	} else {
		visibility = View.GONE
	}
}

fun View.slideExit() {
	if (translationY == 0f) animate().translationY(-height.toFloat())
}

fun View.slideEnter() {
	if (translationY < 0f) animate().translationY(0f)
}