package com.babestudios.companyinfouk.common.ext

import android.content.Context
import android.content.res.ColorStateList
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat

fun Context.color(res: Int): Int = ContextCompat.getColor(this, res)

fun Context.colorStateList(res: Int): ColorStateList = ColorStateList.valueOf(this.color(res))

fun Context.resolveAttribute(@AttrRes attributeResId: Int): Int {
	val typedValue = TypedValue()
	if (this.theme.resolveAttribute(attributeResId, typedValue, true)) {
		return typedValue.data
	}
	throw IllegalArgumentException(this.resources.getResourceName(attributeResId))
}
