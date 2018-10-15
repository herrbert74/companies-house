package com.babestudios.companyinfouk.ui.officerappointments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointment;
import com.babestudios.companyinfouk.data.model.officers.appointments.Appointments;

import butterknife.BindView;
import butterknife.ButterKnife;

class OfficerAppointmentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_APPOINTMENT = 1;

	private Appointments appointments;

	private AppointmentsRecyclerViewClickListener mItemListener;

	OfficerAppointmentsAdapter(Context c, Appointments appointments) {
		this.appointments = appointments;
		mItemListener = (AppointmentsRecyclerViewClickListener) c;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView;
		if (viewType == TYPE_APPOINTMENT) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.officer_appointments_list_item, parent, false);
			return new TransactionsViewHolder(itemLayoutView);
		} else if (viewType == TYPE_HEADER) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.officer_appointments_header_item, parent, false);

			return new HeaderViewHolder(itemLayoutView);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof HeaderViewHolder) {
			((HeaderViewHolder) viewHolder).textViewOfficerName.setText(appointments.name);
		} else if (viewHolder instanceof TransactionsViewHolder) {
			((TransactionsViewHolder) viewHolder).textViewAppointedOn.setText(appointments.items.get(position - 1).appointedOn);
			((TransactionsViewHolder) viewHolder).textViewCompanyName.setText(appointments.items.get(position - 1).appointedTo.companyName);
			((TransactionsViewHolder) viewHolder).textViewCompanyStatus.setText(appointments.items.get(position - 1).appointedTo.companyStatus);
			((TransactionsViewHolder) viewHolder).textViewRole.setText(appointments.items.get(position - 1).officerRole);
			if (appointments.items.get(position - 1).resignedOn != null) {
				((TransactionsViewHolder) viewHolder).textViewResignedOn.setText(appointments.items.get(position - 1).resignedOn);
			} else {
				((TransactionsViewHolder) viewHolder).textViewResignedOn.setVisibility(View.GONE);
				((TransactionsViewHolder) viewHolder).textViewLabelResignedOn.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else {
			return TYPE_APPOINTMENT;
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
		return appointments.items.size() + 1;
	}

	public class HeaderViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textViewOfficerName)
		TextView textViewOfficerName;

		HeaderViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);

		}
	}

	public class TransactionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		@BindView(R.id.textViewAppointedOn)
		TextView textViewAppointedOn;
		@BindView(R.id.textViewCompanyName)
		TextView textViewCompanyName;
		@BindView(R.id.textViewCompanyStatus)
		TextView textViewCompanyStatus;
		@BindView(R.id.textViewRole)
		TextView textViewRole;
		@BindView(R.id.textViewLabelResignedOn)
		TextView textViewLabelResignedOn;
		@BindView(R.id.textViewResignedOn)
		TextView textViewResignedOn;

		TransactionsViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			mItemListener.appointmentItemClicked(v, this.getLayoutPosition(), appointments.items.get(getLayoutPosition() - 1));
		}
	}

	interface AppointmentsRecyclerViewClickListener {
		void appointmentItemClicked(View v, int position, Appointment item);
	}
}
