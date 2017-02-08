package com.babestudios.companyinfouk.ui.filinghistory;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;

import com.babestudios.companyinfouk.BuildConfig;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.ui.search.SearchPresenter;

import net.grandcentrix.thirtyinch.TiPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observer;

public class FilingHistoryPresenter extends TiPresenter<FilingHistoryActivityView> implements Observer<FilingHistoryList> {

	enum CategoryFilter {
		CATEGORY_SHOW_ALL ("all"),
		CATEGORY_GAZETTE("gazette"),
		CATEGORY_CONFIRMATION_STATEMENT("confirmation-statement"),
		CATEGORY_ACCOUNTS("accounts"),
		CATEGORY_ANNUAL_RETURN("annual return"),
		CATEGORY_OFFICERS("officers"),
		CATEGORY_ADDRESS("address"),
		CATEGORY_CAPITAL("capital"),
		CATEGORY_INSOLVENCY("insolvency"),
		CATEGORY_OTHER("other"),
		CATEGORY_INCORPORATION("incorporation"),
		CATEGORY_CONSTITUTION("change-of-constitution"),
		CATEGORY_AUDITORS("auditors"),
		CATEGORY_RESOLUTION("resolution"),
		CATEGORY_MORTGAGE("mortgage");

		private final String name;

		CategoryFilter(final String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	DataManager dataManager;

	private FilingHistoryList filingHistoryList;

	private CategoryFilter categoryFilter = CategoryFilter.CATEGORY_SHOW_ALL;

	@Inject
	public FilingHistoryPresenter(DataManager dataManager) {
		this.dataManager = dataManager;
	}

	@Override
	protected void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onAttachView(@NonNull FilingHistoryActivityView view) {
		super.onAttachView(view);
		if(filingHistoryList != null) {
			view.showFilingHistory(filingHistoryList, categoryFilter);
			view.setInitialCategoryFilter(categoryFilter);
		} else {
			view.showProgress();
			getFilingHistory(view.getCompanyNumber(), view.getFilingCategory());
		}
	}

	@VisibleForTesting
	public void getFilingHistory(String companyNumber, String category) {
		dataManager.getFilingHistory(companyNumber, category, "0").subscribe(this);
	}

	public void loadMoreFilingHistory(int page) {
		dataManager.getFilingHistory(getView().getCompanyNumber(), getView().getFilingCategory(), String.valueOf(page * Integer.valueOf(BuildConfig.COMPANIES_HOUSE_SEARCH_ITEMS_PER_PAGE))).subscribe(this);
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		Log.d("test", "onError: " + e.fillInStackTrace());
		if(getView()!= null) {
			getView().showError();
		}
	}

	@Override
	public void onNext(FilingHistoryList filingHistoryList) {
		this.filingHistoryList = filingHistoryList;
		getView().hideProgress();
		getView().showFilingHistory(filingHistoryList, categoryFilter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	void setCategoryFilter(int category) {
		categoryFilter = CategoryFilter.values()[category];
		if (getView() != null) {
			getView().setFilterOnAdapter(categoryFilter);
		}
	}

	public static Spannable createSpannableDescription(String s, FilingHistoryItem filingHistoryItem) {
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

}
