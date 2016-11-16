package com.babestudios.companieshouse.ui.filinghistory;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilingHistoryAdapter extends RecyclerView.Adapter<FilingHistoryAdapter.FilingHistoryViewHolder> {

	private FilingHistoryRecyclerViewClickListener mItemListener;

	private FilingHistoryList filingHistoryList = new FilingHistoryList();

	DataManager dataManager;

	FilingHistoryAdapter(Context c, FilingHistoryList filingHistoryList, DataManager dataManager) {
		mItemListener = (FilingHistoryRecyclerViewClickListener) c;
		this.filingHistoryList = filingHistoryList;
		this.dataManager = dataManager;
	}

	@Override
	public FilingHistoryViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.filing_history_list_item, parent, false);

		return new FilingHistoryViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(FilingHistoryViewHolder viewHolder, int position) {

		viewHolder.lblDescription.setText(dataManager.filingHistoryLookup(filingHistoryList.items.get(position).description));
		viewHolder.lblDate.setText(filingHistoryList.items.get(position).date);
		viewHolder.lblCategory.setText(filingHistoryList.items.get(position).category);
		viewHolder.lblType.setText(filingHistoryList.items.get(position).type);

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return filingHistoryList.items.size();
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
			//mItemListener.searchResultItemClicked(v, this.getLayoutPosition(), filingHistoryList.items.get(getLayoutPosition()).title, filingHistoryList.items.get(getLayoutPosition()).companyNumber);
		}
	}

	interface FilingHistoryRecyclerViewClickListener {
		void searchResultItemClicked(View v, int position, String companyName, String companyNumber);
	}

	void addItems(FilingHistoryList filingHistoryList) {
		this.filingHistoryList.items.addAll(filingHistoryList.items);
		notifyDataSetChanged();
	}
}
