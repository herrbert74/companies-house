package com.babestudios.companyinfouk.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentSearchesResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_ITEM = 1;

	private RecentSearchesRecyclerViewClickListener mItemListener;

	private ArrayList<SearchHistoryItem> searchHistoryItems = new ArrayList<>();

	public RecentSearchesResultsAdapter(Context c, ArrayList<SearchHistoryItem> searchHistoryItems) {
		mItemListener = (RecentSearchesRecyclerViewClickListener) c;
		this.searchHistoryItems = searchHistoryItems;
	}

	public void refreshData(ArrayList<SearchHistoryItem> searchHistoryItems) {
		this.searchHistoryItems = searchHistoryItems;
		notifyDataSetChanged();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == TYPE_ITEM) {
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.recent_searches_list_item, parent, false);

			return new RecentSearchesViewHolder(itemLayoutView);
		} else if (viewType == TYPE_HEADER) {
			View itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.recent_searches_title_list_item, parent, false);

			return new TitleViewHolder(itemLayoutView);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof RecentSearchesViewHolder) {
			((RecentSearchesViewHolder) viewHolder).lblCompanyName.setText(searchHistoryItems.get(searchHistoryItems.size() - position).companyName);
			((RecentSearchesViewHolder) viewHolder).lblCompanyNumber.setText(searchHistoryItems.get(searchHistoryItems.size() - position).companyNumber);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position))
			return TYPE_HEADER;

		return TYPE_ITEM;
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return searchHistoryItems.size() + 1;
	}

	class TitleViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.lblTitle)
		TextView lblTitle;

		public TitleViewHolder(View itemView) {
			super(itemView);
		}
	}


	class RecentSearchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@BindView(R.id.lblCompanyName)
		TextView lblCompanyName;
		@BindView(R.id.lblCompanyNumber)
		TextView lblCompanyNumber;

		RecentSearchesViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.recentSearchesResultItemClicked(v, this.getLayoutPosition(), searchHistoryItems.get(searchHistoryItems.size() - getLayoutPosition()).companyName, searchHistoryItems.get(searchHistoryItems.size() - getLayoutPosition()).companyNumber);
		}
	}

	interface RecentSearchesRecyclerViewClickListener {
		void recentSearchesResultItemClicked(View v, int position, String companyName, String companyNumber);
	}
}
