package com.babestudios.companyinfouk.ui.chargesdetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.charges.ChargesItem;

import butterknife.BindView;
import butterknife.ButterKnife;

class ChargesDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_TRANSACTION = 1;

	private ChargesItem chargesItem;

	Context context;

	ChargesDetailsAdapter(Context c, ChargesItem chargesItem) {
		this.chargesItem = chargesItem;
		context = c;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView;
		if (viewType == TYPE_TRANSACTION) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.charges_details_transactions_list_item, parent, false);

			return new TransactionsViewHolder(itemLayoutView);
		} else if (viewType == TYPE_HEADER) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.charges_details_header_item, parent, false);

			return new HeaderViewHolder(itemLayoutView);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof HeaderViewHolder) {
			((HeaderViewHolder) viewHolder).textViewDeliveredOn.setText(chargesItem.deliveredOn);
			((HeaderViewHolder) viewHolder).textViewStatus.setText(chargesItem.status);
			((HeaderViewHolder) viewHolder).textViewContainsFloatingCharge.setText(chargesItem.particulars.containsFloatingCharge ? "YES" : "NO");
			((HeaderViewHolder) viewHolder).textViewFloatingChargeCoversAll.setText(chargesItem.particulars.floatingChargeCoversAll ? "YES" : "NO");
			((HeaderViewHolder) viewHolder).textViewContainsNegativePledge.setText(chargesItem.particulars.containsNegativePledge ? "YES" : "NO");
			((HeaderViewHolder) viewHolder).textViewContainsFixedCharge.setText(chargesItem.particulars.containsFixedCharge ? "YES" : "NO");
			if(chargesItem.satisfiedOn == null) {
				((HeaderViewHolder) viewHolder).textViewLabelSatisfiedOn.setVisibility(View.GONE);
				((HeaderViewHolder) viewHolder).textViewSatisfiedOn.setVisibility(View.GONE);
			}else{
				((HeaderViewHolder) viewHolder).textViewSatisfiedOn.setVisibility(View.VISIBLE);
				((HeaderViewHolder) viewHolder).textViewLabelSatisfiedOn.setVisibility(View.VISIBLE);
				((HeaderViewHolder) viewHolder).textViewSatisfiedOn.setText(chargesItem.satisfiedOn);
			}
			((HeaderViewHolder) viewHolder).textViewPersonsEntitled.setText(chargesItem.personsEntitled.get(0).name);

		} else if (viewHolder instanceof TransactionsViewHolder) {
			((TransactionsViewHolder) viewHolder).textViewFilingType.setText(chargesItem.transactions.get(position - 1).filingType);
			((TransactionsViewHolder) viewHolder).textViewDeliveredOn.setText(chargesItem.transactions.get(position - 1).deliveredOn);
		}

	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else {
			return TYPE_TRANSACTION;
		}
	}

	private boolean isPositionHeader(int position) {
		return position == 0;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return chargesItem.transactions.size() + 1;
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textViewDeliveredOn)
		TextView textViewDeliveredOn;
		@BindView(R.id.textViewStatus)
		TextView textViewStatus;
		@BindView(R.id.textViewContainsFloatingCharge)
		TextView textViewContainsFloatingCharge;
		@BindView(R.id.textViewFloatingChargeCoversAll)
		TextView textViewFloatingChargeCoversAll;
		@BindView(R.id.textViewContainsNegativePledge)
		TextView textViewContainsNegativePledge;
		@BindView(R.id.textViewContainsFixedCharge)
		TextView textViewContainsFixedCharge;
		@BindView(R.id.textViewLabelSatisfiedOn)
		TextView textViewLabelSatisfiedOn;
		@BindView(R.id.textViewSatisfiedOn)
		TextView textViewSatisfiedOn;
		@BindView(R.id.textViewPersonsEntitled)
		TextView textViewPersonsEntitled;

		HeaderViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public class TransactionsViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textViewFilingType)
		TextView textViewFilingType;
		@BindView(R.id.textViewDeliveredOn)
		TextView textViewDeliveredOn;


		TransactionsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
