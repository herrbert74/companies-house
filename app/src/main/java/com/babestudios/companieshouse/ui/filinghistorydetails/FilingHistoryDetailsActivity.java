package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryItem;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilingHistoryDetailsActivity extends TiActivity<FilingHistoryDetailsPresenter, FilingHistoryDetailsActivityView> implements FilingHistoryDetailsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Bind(R.id.textViewDate)
	TextView textViewDate;

	@Bind(R.id.textViewCategory)
	TextView textViewCategory;

	@Bind(R.id.textViewDescription)
	TextView textViewDescription;

	@Singleton
	@Inject
	DataManager dataManager;

	String filingHistoryItemString;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filing_history_details);

		ButterKnife.bind(this);
		filingHistoryItemString = getIntent().getStringExtra("filingHistoryItem");
		Gson gson = new Gson();
		FilingHistoryItem filingHistoryItem = gson.fromJson(filingHistoryItemString, FilingHistoryItem.class);
		textViewDate.setText(filingHistoryItem.date);
		textViewCategory.setText(filingHistoryItem.category);
		textViewDescription.setText(filingHistoryItem.description);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.filing_history_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}

	}

	@Override
	public void showProgress() {
		progressbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressbar.setVisibility(View.GONE);
	}

	@NonNull
	@Override
	public FilingHistoryDetailsPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new FilingHistoryDetailsPresenter(dataManager);
	}
}
