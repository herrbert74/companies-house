package com.babestudios.companieshouse.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * This version won't draw a lie under the sub header, the first item
 */

public class DividerItemDecorationWithSubHeading extends RecyclerView.ItemDecoration {

	private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

	private Drawable mDivider;

	private ArrayList<Integer> titlePositions;

	/**
	 * Default divider will be used
	 */
	public DividerItemDecorationWithSubHeading(Context context, ArrayList<Integer> titlePositions) {
		final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
		this.titlePositions = titlePositions;
		mDivider = styledAttributes.getDrawable(0);
		styledAttributes.recycle();
	}

	/**
	 * Custom divider will be used
	 */
	public DividerItemDecorationWithSubHeading(Context context, int resId) {
		mDivider = ContextCompat.getDrawable(context, resId);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (!titlePositions.contains(i)) {
				View child = parent.getChildAt(i);

				RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

				int top = child.getBottom() + params.bottomMargin;
				int bottom = top + mDivider.getIntrinsicHeight();

				mDivider.setBounds(left, top, right, bottom);
				mDivider.draw(c);
			}
		}
	}
}
