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
		if(filingHistoryItem.description.equals("legacy")){
			viewHolder.lblDescription.setText(filingHistoryItem.descriptionValues.description);
		} else {
			Spannable spannableDescription = createSpannableDescription(dataManager.filingHistoryLookup(filingHistoryItem.description), filingHistoryItem);
			viewHolder.lblDescription.setText(spannableDescription);
		}
		viewHolder.lblDate.setText(filingHistoryItem.date);
		viewHolder.lblCategory.setText(filingHistoryItem.category);
		viewHolder.lblType.setText(filingHistoryItem.type);

	}

	private Spannable createSpannableDescription(String s, FilingHistoryItem filingHistoryItem) {
		if (s != null) {
			int first = s.indexOf("**");
			int second = s.indexOf("**", first + 1) - 2;

			s = s.replace("**", "");

			ArrayList<Pair> spanPairs = new ArrayList<>();
			int spanFirst;
			if (filingHistoryItem.descriptionValues != null) {
				String officerName = filingHistoryItem.descriptionValues.officerName;
				if (officerName != null) {
					s = s.replace("**", "").replace("{officer_name}", officerName);
					spanFirst = s.indexOf(officerName);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + officerName.length()));
				}
				String appointmentDate = filingHistoryItem.descriptionValues.appointmentDate;
				if (appointmentDate != null) {
					s = s.replace("{appointment_date}", appointmentDate);
					spanFirst = s.indexOf(appointmentDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + appointmentDate.length()));
				}
				String madeUpDate = filingHistoryItem.descriptionValues.madeUpDate;
				if (madeUpDate != null) {
					s = s.replace("{made_up_date}", madeUpDate);
					spanFirst = s.indexOf(madeUpDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + madeUpDate.length()));
				}
				String terminationDate = filingHistoryItem.descriptionValues.terminationDate;
				if (terminationDate != null) {
					s = s.replace("{termination_date}", terminationDate);
					spanFirst = s.indexOf(terminationDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + terminationDate.length()));
				}
				String newDate = filingHistoryItem.descriptionValues.newDate;
				if (newDate != null) {
					s = s.replace("{new_date}", newDate);
					spanFirst = s.indexOf(newDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + newDate.length()));
				}
				String changeDate = filingHistoryItem.descriptionValues.changeDate;
				if (changeDate != null) {
					s = s.replace("{change_date}", changeDate);
					spanFirst = s.indexOf(changeDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + changeDate.length()));
				}
				String oldAddress = filingHistoryItem.descriptionValues.oldAddress;
				if (oldAddress != null) {
					s = s.replace("{old_address}", oldAddress);
					spanFirst = s.indexOf(oldAddress);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + oldAddress.length()));
				}
				String newAddress = filingHistoryItem.descriptionValues.newAddress;
				if (newAddress != null) {
					s = s.replace("{new_address}", newAddress);
					spanFirst = s.indexOf(newAddress);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + newAddress.length()));
				}
				String formAttached = filingHistoryItem.descriptionValues.formAttached;
				if (formAttached != null) {
					s = s.replace("{form_attached}", formAttached);
					spanFirst = s.indexOf(formAttached);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + formAttached.length()));
				}
				String chargeNumber = filingHistoryItem.descriptionValues.chargeNumber;
				if (chargeNumber != null) {
					s = s.replace("{charge_number}", chargeNumber);
					spanFirst = s.indexOf(chargeNumber);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + chargeNumber.length()));
				}
				String chargeCreationDate = filingHistoryItem.descriptionValues.chargeCreationDate;
				if (chargeCreationDate != null) {
					s = s.replace("{charge_creation_date}", chargeCreationDate);
					spanFirst = s.indexOf(chargeCreationDate);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + chargeCreationDate.length()));
				}
				String date = filingHistoryItem.descriptionValues.date;
				if (date != null) {
					s = s.replace("{date}", date);
					spanFirst = s.indexOf(date);
					spanPairs.add(new Pair<>(spanFirst, spanFirst + date.length()));
				}

			}
			Spannable spannable = new SpannableString(s);
			StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
			spannable.setSpan(boldSpan, first, second, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			for (Pair pair : spanPairs) {
				StyleSpan boldSpan2 = new StyleSpan(Typeface.BOLD);
				if ((int) pair.first > -1 && (int) pair.second > -1) {
					spannable.setSpan(boldSpan2, (int) pair.first, (int) pair.second, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				}
			}
			return spannable;
		}
		return null;
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
			mItemListener.filingItemClicked(v, this.getLayoutPosition(), filingHistoryList.items.get(getLayoutPosition()));
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
