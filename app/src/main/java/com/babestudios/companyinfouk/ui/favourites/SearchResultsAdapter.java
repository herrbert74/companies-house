package com.babestudios.companyinfouk.ui.favourites;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

	private SearchResultsRecyclerViewClickListener mItemListener;

	private CompanySearchResult companySearchResults = new CompanySearchResult();

	SearchResultsAdapter(Context c, CompanySearchResult companySearchResults) {
		mItemListener = (SearchResultsRecyclerViewClickListener) c;
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
		@BindView(R.id.lblAddress)
		TextView lblAddress;
		@BindView(R.id.lblCompanyName)
		TextView lblCompanyName;
		@BindView(R.id.lblActiveStatus)
		TextView lblActiveStatus;
		@BindView(R.id.lblIncorporated)
		TextView lblIncorporated;

		SearchResultsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.searchResultItemClicked(v, this.getLayoutPosition(), companySearchResults.items.get(getLayoutPosition()).title, companySearchResults.items.get(getLayoutPosition()).companyNumber);
		}
	}

	interface SearchResultsRecyclerViewClickListener {
		void searchResultItemClicked(View v, int position, String companyName, String companyNumber);
	}

	void addItems(CompanySearchResult companySearchResults) {
		this.companySearchResults.items.addAll(companySearchResults.items);
		notifyDataSetChanged();
	}
}
