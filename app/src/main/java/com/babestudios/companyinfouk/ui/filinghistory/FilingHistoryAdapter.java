package com.babestudios.companyinfouk.ui.filinghistory;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResultItem;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.schedulers.Schedulers;

public class FilingHistoryAdapter extends RecyclerView.Adapter<FilingHistoryAdapter.FilingHistoryViewHolder> {

	private FilingHistoryRecyclerViewClickListener mItemListener;

	private FilingHistoryList filingHistoryList = new FilingHistoryList();
	private List<FilingHistoryItem> filteredFilingHistoryItems;

	@Inject
	DataManager dataManager;

	FilingHistoryAdapter(Context c, FilingHistoryList filingHistoryList, FilingHistoryPresenter.CategoryFilter categoryFilter) {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		mItemListener = (FilingHistoryRecyclerViewClickListener) c;
		this.filingHistoryList = filingHistoryList;
		updateFilteredResults(filingHistoryList, categoryFilter);
	}

	private void updateFilteredResults(FilingHistoryList filingHistoryList, FilingHistoryPresenter.CategoryFilter categoryFilter) {
		if (categoryFilter.ordinal() > FilingHistoryPresenter.CategoryFilter.CATEGORY_SHOW_ALL.ordinal()) {
			Observable.from(filingHistoryList.items).filter(filingHistoryItem -> filingHistoryItem.category.equalsIgnoreCase(categoryFilter.toString()))
					.observeOn(Schedulers.immediate())
					.toList()
					.subscribe(result -> filteredFilingHistoryItems = result);
		} else {
			filteredFilingHistoryItems = filingHistoryList.items;
		}
	}

	@Override
	public FilingHistoryViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.filing_history_list_item, parent, false);

		return new FilingHistoryViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(FilingHistoryViewHolder viewHolder, int position) {
		FilingHistoryItem filingHistoryItem = filteredFilingHistoryItems.get(position);
		if(filingHistoryItem.description.equals("legacy") || filingHistoryItem.description.equals("miscellaneous")){
			viewHolder.lblDescription.setText(filingHistoryItem.descriptionValues.description);
		} else {
			Spannable spannableDescription = FilingHistoryPresenter.createSpannableDescription(dataManager.filingHistoryLookup(filingHistoryItem.description), filingHistoryItem);
			viewHolder.lblDescription.setText(spannableDescription);
		}
		viewHolder.lblDate.setText(filingHistoryItem.date);
		viewHolder.lblCategory.setText(filingHistoryItem.category);
		viewHolder.lblType.setText(filingHistoryItem.type);

	}



	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return filteredFilingHistoryItems.size();
	}

	public void setFilterOnAdapter(FilingHistoryPresenter.CategoryFilter categoryFilter) {
		updateFilteredResults(filingHistoryList, categoryFilter);
		notifyDataSetChanged();
	}

	class FilingHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblDate)
		TextView lblDate;
		@Bind(R.id.lblDescription)
		TextView lblDescription;
		@Bind(R.id.lblCategory)
		TextView lblCategory;
		@Bind(R.id.lblType)
		TextView lblType;

		FilingHistoryViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.filingItemClicked(v, this.getLayoutPosition(), filteredFilingHistoryItems.get(getLayoutPosition()));
		}
	}

	interface FilingHistoryRecyclerViewClickListener {
		void filingItemClicked(View v, int position, FilingHistoryItem item);
	}

	void updateItems(FilingHistoryList filingHistoryList) {
		this.filingHistoryList = filingHistoryList;
		notifyDataSetChanged();
	}
}
