package com.babestudios.companyinfouk.ui.charges;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.charges.Charges;
import com.babestudios.companyinfouk.data.model.charges.ChargesItem;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesAdapter extends RecyclerView.Adapter<ChargesAdapter.ChargesViewHolder> {

	private ChargesRecyclerViewClickListener mItemListener;

	private Charges charges = new Charges();

	DataManager dataManager;

	ChargesAdapter(Context c, Charges charges, DataManager dataManager) {
		mItemListener = (ChargesRecyclerViewClickListener) c;
		this.charges = charges;
		this.dataManager = dataManager;
	}

	@Override
	public ChargesViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.charges_list_item, parent, false);

		return new ChargesViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(ChargesViewHolder viewHolder, int position) {

		viewHolder.lblCreatedOn.setText(charges.items.get(position).createdOn);
		viewHolder.lblChargeCode.setText(charges.items.get(position).chargeCode);
		viewHolder.lblStatus.setText(charges.items.get(position).status);
		viewHolder.lblPersonEntitled.setText(charges.items.get(position).personsEntitled.get(0).name);

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return charges.items.size();
	}

	class ChargesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblCreatedOn)
		TextView lblCreatedOn;
		@Bind(R.id.lblStatus)
		TextView lblStatus;
		@Bind(R.id.lblChargeCode)
		TextView lblChargeCode;
		@Bind(R.id.lblPersonEntitled)
		TextView lblPersonEntitled;

		ChargesViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.chargesItemClicked(v, this.getLayoutPosition(), charges.items.get(getLayoutPosition()));
		}
	}

	interface ChargesRecyclerViewClickListener {
		void chargesItemClicked(View v, int position, ChargesItem chargesItem);
	}

	void updateItems(Charges charges) {
		this.charges = charges;
		notifyDataSetChanged();
	}
}
