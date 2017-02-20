package com.babestudios.companyinfouk.ui.insolvency;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency;
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsolvencyAdapter extends RecyclerView.Adapter<InsolvencyAdapter.InsolvencyViewHolder> {

	private InsolvencyRecyclerViewClickListener mItemListener;

	private Insolvency insolvency = new Insolvency();

	InsolvencyAdapter(Context c, Insolvency insolvency) {
		mItemListener = (InsolvencyRecyclerViewClickListener) c;
		this.insolvency = insolvency;
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
		if(insolvency.cases.get(position).practitioners.size() > 0) {
			viewHolder.lblPractitioner.setText(insolvency.cases.get(position).practitioners.get(0).name);
		}

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
			mItemListener.insolvencyItemClicked(v, this.getLayoutPosition(), insolvency.cases.get(getLayoutPosition()));
		}
	}

	interface InsolvencyRecyclerViewClickListener {
		void insolvencyItemClicked(View v, int position, InsolvencyCase insolvencyCase);
	}

	void updateItems(Insolvency insolvency) {
		this.insolvency = insolvency;
		notifyDataSetChanged();
	}
}
