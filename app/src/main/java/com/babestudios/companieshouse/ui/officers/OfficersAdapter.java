package com.babestudios.companieshouse.ui.officers;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.OfficerItem;
import com.babestudios.companieshouse.data.model.officers.Officers;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfficersAdapter extends RecyclerView.Adapter<OfficersAdapter.OfficersViewHolder> {

	private OfficersRecyclerViewClickListener mItemListener;

	private Officers officers = new Officers();

	DataManager dataManager;

	OfficersAdapter(Context c, Officers officers, DataManager dataManager) {
		mItemListener = (OfficersRecyclerViewClickListener) c;
		this.officers = officers;
		this.dataManager = dataManager;
	}

	@Override
	public OfficersViewHolder onCreateViewHolder(ViewGroup parent, int i) {
		View itemLayoutView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.officers_list_item, parent, false);

		return new OfficersViewHolder(itemLayoutView);
	}

	@Override
	public void onBindViewHolder(OfficersViewHolder viewHolder, int position) {

		viewHolder.lblName.setText(officers.items.get(position).name);
		viewHolder.lblAppointedOn.setText(officers.items.get(position).appointedOn);
		viewHolder.lblRole.setText(officers.items.get(position).officerRole);
		viewHolder.lblResignedOn.setText(officers.items.get(position).resignedOn);

	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return officers.items.size();
	}

	class OfficersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Bind(R.id.lblName)
		TextView lblName;
		@Bind(R.id.lblAppointedOn)
		TextView lblAppointedOn;
		@Bind(R.id.lblRole)
		TextView lblRole;
		@Bind(R.id.lblResignedOn)
		TextView lblResignedOn;

		OfficersViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.officersItemClicked(v, this.getLayoutPosition(), officers.items.get(getLayoutPosition()));
		}
	}

	interface OfficersRecyclerViewClickListener {
		void officersItemClicked(View v, int position, OfficerItem officerItem);
	}

	void updateItems(Officers officers) {
		this.officers = officers;
		notifyDataSetChanged();
	}
}
