package com.babestudios.companyinfouk.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat

object ResourceHelper {

	fun getBitmap(context: Context, drawableId: Int): Bitmap {
		val drawable = ContextCompat.getDrawable(context, drawableId)
		return if (drawable is BitmapDrawable) {
			BitmapFactory.decodeResource(context.resources, drawableId)
		} else if (drawable is VectorDrawableCompat) {
			getBitmap((drawable as VectorDrawableCompat?)!!)
		} else {
			throw IllegalArgumentException("unsupported drawable type")
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	fun getBitmap(vectorDrawable: VectorDrawableCompat): Bitmap {
		val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
		vectorDrawable.draw(canvas)
		return bitmap
	}

	fun tintVectorDrawableCompat(context: Context, drawable: VectorDrawableCompat, colorId: Int): Drawable {
		drawable.setTint(ContextCompat.getColor(context, colorId))
		return drawable
	}

	fun tintDrawable(context: Context, drawableId: Int, colorId: Int): Drawable {
		val drawable = ResourcesCompat.getDrawable(context.resources, drawableId, null)
		return tintDrawable(context, drawable, colorId)
	}

	fun tintDrawable(context: Context, drawable: Drawable?, colorId: Int): Drawable {
		var drawable = drawable
		drawable = DrawableCompat.wrap(drawable!!)
		DrawableCompat.setTint(drawable!!.mutate(), ContextCompat.getColor(context, colorId))
		return drawable
	}
}
