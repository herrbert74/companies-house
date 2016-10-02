package com.babestudios.companieshouse.ui.search;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentSearchesResultsAdapter extends RecyclerView.Adapter<RecentSearchesResultsAdapter.RecentSearchesViewHolder> {

	private RecentSearchesRecyclerViewClickListener mItemListener;

	private ArrayList<SearchHistoryItem> searchHistoryItems = new ArrayList<>();

	public RecentSearchesResultsAdapter(Context c, ArrayList<SearchHistoryItem> searchHistoryItems) {
		//((CompaniesHouseApplication) ((SearchActivity)c).getApplication()).getApplicationComponent().inject(this);
		mItemListener = (RecentSearchesRecyclerViewClickListener) c;
		this.searchHistoryItems = searchHistoryItems;
	}

	@Override
	public RecentSearchesViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recent_searches_list_item, parent, false);

		return new RecentSearchesViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(RecentSearchesViewHolder viewHolder, int position) {
		viewHolder.lblCompanyName.setText(searchHistoryItems.get(position).companyName);
		viewHolder.lblCompanyNumber.setText(searchHistoryItems.get(position).companyNumber);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return searchHistoryItems.size();
	}

	class RecentSearchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblCompanyName)
		TextView lblCompanyName;
		@Bind(R.id.lblCompanyNumber)
		TextView lblCompanyNumber;

		RecentSearchesViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.recentSearchesResultItemClicked(v, this.getLayoutPosition(), searchHistoryItems.get(getLayoutPosition()).companyName, searchHistoryItems.get(getLayoutPosition()).companyNumber);
		}
	}

	interface RecentSearchesRecyclerViewClickListener {
		void recentSearchesResultItemClicked(View v, int position, String companyName, String companyNumber);
	}
}
