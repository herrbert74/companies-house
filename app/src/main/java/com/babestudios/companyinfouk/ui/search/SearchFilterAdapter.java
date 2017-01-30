package com.babestudios.companyinfouk.ui.search;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.utils.ResourceHelper;


public class SearchFilterAdapter extends ArrayAdapter<String> {
	private String[] mTexts;
	private boolean isDarkTheme;
	private Context context;
	private int verticalDropdownPadding;

	public SearchFilterAdapter(Context context, String[] texts, boolean isDarkTheme) {
		super(context, R.layout.search_filter_spinner_item, texts);
		this.isDarkTheme = isDarkTheme;
		mTexts = texts;
		this.context = context;
		verticalDropdownPadding = getContext().getResources().getDimensionPixelSize(R.dimen.view_margin_small);
	}

	@Override
	public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
		DropDownViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.search_filter_spinner_dropdown_item, parent, false);
			holder = new DropDownViewHolder();
			holder.dropdown_root = convertView.findViewById(R.id.dropdown_item_root);
			holder.dropdown_text_view = (TextView) convertView.findViewById(R.id.dropdown_item_text_view);
			convertView.setTag(holder);
		} else {
			holder = (DropDownViewHolder) convertView.getTag();
		}
		int bottomPadding = 0;
		int topPadding = 0;
		if (position == 0) {
			topPadding = verticalDropdownPadding;
		}
		if (position == (getCount() - 1)) {
			bottomPadding = verticalDropdownPadding;
		}

		if (isDarkTheme) {
			holder.dropdown_root.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
		}

		holder.dropdown_root.setPadding(0, topPadding, 0, bottomPadding);
		holder.dropdown_text_view.setText(mTexts[position]);

		return convertView;
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		SpinnerItemViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.search_filter_spinner_item, parent, false);
			holder = new SpinnerItemViewHolder();
			holder.spinner_item_root = convertView.findViewById(R.id.spinner_item_root);
			holder.spinner_item_icon = (ImageView) convertView.findViewById(R.id.spinner_item_icon);
			holder.spinner_item_text_view = (TextView) convertView.findViewById(R.id.spinner_item_text_view);
			if (isDarkTheme) {
				holder.spinner_item_text_view.setTextColor(ContextCompat.getColor(context, android.R.color.white));
				holder.spinner_item_icon.setImageDrawable(ResourceHelper.tintVectorDrawableCompat(context, VectorDrawableCompat.create
						(context.getResources(), R.drawable.arrow_drop_down_vector, context.getTheme()), android.R.color.white));
			}
			convertView.setTag(holder);
		} else {
			holder = (SpinnerItemViewHolder) convertView.getTag();
		}


		holder.spinner_item_text_view.setText(mTexts[position]);
		return convertView;
	}

	private static class DropDownViewHolder {
		View dropdown_root;
		TextView dropdown_text_view;
	}

	private static class SpinnerItemViewHolder {
		View spinner_item_root;
		ImageView spinner_item_icon;
		TextView spinner_item_text_view;
	}
}
