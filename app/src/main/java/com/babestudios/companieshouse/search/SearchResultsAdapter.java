package com.babestudios.companieshouse.search;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.search.pojos.CompanySearchResult;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

	private RecyclerViewClickListener mItemListener;

	private CompanySearchResult companySearchResults = new CompanySearchResult();

	SearchResultsAdapter(Context c, CompanySearchResult companySearchResults) {
		((CompaniesHouseApplication) ((SearchActivity)c).getApplication()).getApplicationComponent().inject(this);
		mItemListener = (RecyclerViewClickListener) c;
		this.companySearchResults = companySearchResults;
	}

	@Override
	public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.search_result_list_item, parent, false);

		return new SearchResultsViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(SearchResultsViewHolder viewHolder, int position) {

		viewHolder.lblCompanyName.setText(companySearchResults.items.get(position).title);
		viewHolder.lblAddress.setText(companySearchResults.items.get(position).addressSnippet);
		viewHolder.lblActiveStatus.setText(companySearchResults.items.get(position).companyStatus);
		viewHolder.lblIncorporated.setText(companySearchResults.items.get(position).description);

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return companySearchResults.items.size();
	}

	class SearchResultsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblAddress)
		TextView lblAddress;
		@Bind(R.id.lblCompanyName)
		TextView lblCompanyName;
		@Bind(R.id.lblActiveStatus)
		TextView lblActiveStatus;
		@Bind(R.id.lblIncorporated)
		TextView lblIncorporated;

		SearchResultsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.searchResultItemClicked(v, this.getLayoutPosition(), companySearchResults.items.get(getLayoutPosition()).description);
		}
	}

	interface RecyclerViewClickListener {
		void searchResultItemClicked(View v, int position, String description);
	}

	void addItems(CompanySearchResult companySearchResults) {
		this.companySearchResults.items.addAll(companySearchResults.items);
		notifyDataSetChanged();
		Log.d("test", "Items size: " + getItemCount());
	}
}
