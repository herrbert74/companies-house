package com.babestudios.companieshouse.ui.insolvency;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsolvencyAdapter extends RecyclerView.Adapter<InsolvencyAdapter.InsolvencyViewHolder> {

	private ChargesRecyclerViewClickListener mItemListener;

	private Insolvency insolvency = new Insolvency();

	DataManager dataManager;

	InsolvencyAdapter(Context c, Insolvency insolvency, DataManager dataManager) {
		mItemListener = (ChargesRecyclerViewClickListener) c;
		this.insolvency = insolvency;
		this.dataManager = dataManager;
	}

	@Override
	public InsolvencyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.insolvency_list_item, parent, false);

		return new InsolvencyViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(InsolvencyViewHolder viewHolder, int position) {

		viewHolder.lblDate.setText(insolvency.cases.get(position).dates.get(0).date);
		viewHolder.lblNumber.setText(insolvency.cases.get(position).number);
		viewHolder.lblType.setText(insolvency.cases.get(position).type);
		viewHolder.lblPractitioner.setText(insolvency.cases.get(position).practitioners.get(0).name);

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return insolvency.cases.size();
	}

	class InsolvencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblDate)
		TextView lblDate;
		@Bind(R.id.lblType)
		TextView lblType;
		@Bind(R.id.lblNumber)
		TextView lblNumber;
		@Bind(R.id.lblPractitioner)
		TextView lblPractitioner;

		InsolvencyViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			//mItemListener.filingItemClicked(v, this.getLayoutPosition(), insolvency.items.get(getLayoutPosition()).title, insolvency.items.get(getLayoutPosition()).companyNumber);
		}
	}

	interface ChargesRecyclerViewClickListener {
		void chargesItemClicked(View v, int position, String companyName, String companyNumber);
	}

	void addItems(Insolvency insolvency) {
		this.insolvency.cases.addAll(insolvency.cases);
		notifyDataSetChanged();
	}
}
