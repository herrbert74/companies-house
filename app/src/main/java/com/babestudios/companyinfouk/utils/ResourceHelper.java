package com.babestudios.companyinfouk.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

public class ResourceHelper {

	public static Bitmap getBitmap(Context context, int drawableId) {
		Drawable drawable = ContextCompat.getDrawable(context, drawableId);
		if (drawable instanceof BitmapDrawable) {
			return BitmapFactory.decodeResource(context.getResources(), drawableId);
		} else if (drawable instanceof VectorDrawableCompat) {
			return getBitmap((VectorDrawableCompat) drawable);
		} else {
			throw new IllegalArgumentException("unsupported drawable type");
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
		Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		vectorDrawable.draw(canvas);
		return bitmap;
	}

	public static Drawable tintVectorDrawableCompat(Context context, VectorDrawableCompat drawable, int colorId) {
		drawable.setTint(ContextCompat.getColor(context, colorId));
		return drawable;
	}

	public static Drawable tintDrawable(Context context, int drawableId, int colorId) {
		Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableId, null);
		return tintDrawable(context, drawable, colorId);
	}

	public static Drawable tintDrawable(Context context, Drawable drawable, int colorId) {
		drawable = DrawableCompat.wrap(drawable);
		DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(context, colorId));
		return drawable;
	}
}
