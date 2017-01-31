package com.babestudios.companyinfouk.ui.search;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultsViewHolder> {

	private SearchResultsRecyclerViewClickListener mItemListener;

	private CompanySearchResult companySearchResults = new CompanySearchResult();
	private List<CompanySearchResultItem> filteredSearchResults;

	SearchResultsAdapter(Context c, CompanySearchResult companySearchResults, SearchPresenter.FilterState filterState) {
		mItemListener = (SearchResultsRecyclerViewClickListener) c;
		this.companySearchResults = companySearchResults;
		updateFilteredResults(companySearchResults, filterState);
	}

	private void updateFilteredResults(CompanySearchResult companySearchResults, SearchPresenter.FilterState filterState) {
		if (filterState.ordinal() > SearchPresenter.FilterState.FILTER_SHOW_ALL.ordinal()) {
			Observable.from(companySearchResults.items).filter(companySearchResultItem -> companySearchResultItem.companyStatus.equalsIgnoreCase(filterState.toString()))
					.observeOn(Schedulers.immediate())
					.toList()
					.subscribe(result -> filteredSearchResults = result);
		} else {
			filteredSearchResults = companySearchResults.items;
		}
	}

	@Override
	public SearchResultsViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.search_result_list_item, parent, false);

		return new SearchResultsViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(SearchResultsViewHolder viewHolder, int position) {
		viewHolder.lblCompanyName.setText(filteredSearchResults.get(position).title);
		viewHolder.lblAddress.setText(filteredSearchResults.get(position).addressSnippet);
		viewHolder.lblActiveStatus.setText(filteredSearchResults.get(position).companyStatus);
		viewHolder.lblIncorporated.setText(filteredSearchResults.get(position).description);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return filteredSearchResults.size();
	}

	void setFilterOnAdapter(SearchPresenter.FilterState filterState) {
		updateFilteredResults(companySearchResults, filterState);
		notifyDataSetChanged();
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
			mItemListener.searchResultItemClicked(v, this.getLayoutPosition(), filteredSearchResults.get(getLayoutPosition()).title, filteredSearchResults.get
					(getLayoutPosition()).companyNumber);
		}
	}

	interface SearchResultsRecyclerViewClickListener {
		void searchResultItemClicked(View v, int position, String companyName, String companyNumber);
	}

	void addItems(CompanySearchResult companySearchResults, SearchPresenter.FilterState filterState) {
		this.companySearchResults.items.addAll(companySearchResults.items);
		updateFilteredResults(companySearchResults, filterState);
		notifyDataSetChanged();
	}
}
