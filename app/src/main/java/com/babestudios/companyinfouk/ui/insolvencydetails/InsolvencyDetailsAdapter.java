package com.babestudios.companyinfouk.ui.insolvencydetails;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.insolvency.Date;
import com.babestudios.companyinfouk.data.model.insolvency.Practitioner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsolvencyDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int TYPE_HEADER = 0;
	private static final int TYPE_DATE = 1;
	private static final int TYPE_PRACTITIONER = 2;

	private List<Date> insolvencyDates = new ArrayList<>();
	private List<Practitioner> insolvencyPractitioners = new ArrayList<>();

	public InsolvencyDetailsAdapter(Context c, List<Date> dates, List<Practitioner> practitioners) {
		this.insolvencyDates = dates;
		this.insolvencyPractitioners = practitioners;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView;
		if (viewType == TYPE_DATE) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.insolvency_details_date_list_item, parent, false);

			return new DatesViewHolder(itemLayoutView);
		} else if (viewType == TYPE_PRACTITIONER) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.insolvency_details_practitioner_list_item, parent, false);

			return new PractitionersViewHolder(itemLayoutView);
		} else if (viewType == TYPE_HEADER) {
			itemLayoutView = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.recent_searches_title_list_item, parent, false);

			return new TitleViewHolder(itemLayoutView);
		}
		throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
		if (viewHolder instanceof TitleViewHolder) {
			if (position == 0) {
				((TitleViewHolder) viewHolder).lblTitle.setText("Dates");
			} else {
				((TitleViewHolder) viewHolder).lblTitle.setText("Practitioners");
			}
		} else if (viewHolder instanceof PractitionersViewHolder) {
			((PractitionersViewHolder) viewHolder).textViewAppointedOn.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).appointedOn);
			((PractitionersViewHolder) viewHolder).textViewCeasedToActOn.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).ceasedToActOn);
			((PractitionersViewHolder) viewHolder).textViewName.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).name);
			((PractitionersViewHolder) viewHolder).textViewRole.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).role);
			((PractitionersViewHolder) viewHolder).textViewAddressLine1.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.addressLine1);
			((PractitionersViewHolder) viewHolder).textViewLocality.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.locality);
			((PractitionersViewHolder) viewHolder).textViewPostalCode.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.postalCode);
			if(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.region == null){
				((PractitionersViewHolder) viewHolder).textViewRegion.setVisibility(View.GONE);
			} else {
				((PractitionersViewHolder) viewHolder).textViewRegion.setVisibility(View.VISIBLE);
				((PractitionersViewHolder) viewHolder).textViewRegion.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.region);
			}
			if(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.country == null){
				((PractitionersViewHolder) viewHolder).textViewCountry.setVisibility(View.GONE);
			} else {
				((PractitionersViewHolder) viewHolder).textViewCountry.setVisibility(View.VISIBLE);
				((PractitionersViewHolder) viewHolder).textViewCountry.setText(insolvencyPractitioners.get(position - insolvencyDates.size() - 2).address.country);
			}
		} else if (viewHolder instanceof DatesViewHolder) {
			((DatesViewHolder) viewHolder).textViewDate.setText(insolvencyDates.get(position - 1).date);
			((DatesViewHolder) viewHolder).textViewType.setText(insolvencyDates.get(position - 1).type);
		}

	}

	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else if (position < insolvencyDates.size() + 1) {
			return TYPE_DATE;
		} else {
			return TYPE_PRACTITIONER;
		}
	}

	private boolean isPositionHeader(int position) {
		return position == 0 || position == insolvencyDates.size() + 1;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return insolvencyDates.size() + insolvencyPractitioners.size() + 2;
	}

	class TitleViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.lblTitle)
		TextView lblTitle;

		public TitleViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	class DatesViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textViewDate)
		TextView textViewDate;
		@BindView(R.id.textViewType)
		TextView textViewType;


		public DatesViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	class PractitionersViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.textViewAppointedOn)
		TextView textViewAppointedOn;
		@BindView(R.id.textViewCeasedToActOn)
		TextView textViewCeasedToActOn;
		@BindView(R.id.textViewName)
		TextView textViewName;
		@BindView(R.id.textViewRole)
		TextView textViewRole;
		@BindView(R.id.textViewAddressLine1)
		TextView textViewAddressLine1;
		@BindView(R.id.textViewLocality)
		TextView textViewLocality;
		@BindView(R.id.textViewPostalCode)
		TextView textViewPostalCode;
		@BindView(R.id.textViewRegion)
		TextView textViewRegion;
		@BindView(R.id.textViewCountry)
		TextView textViewCountry;

		PractitionersViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
