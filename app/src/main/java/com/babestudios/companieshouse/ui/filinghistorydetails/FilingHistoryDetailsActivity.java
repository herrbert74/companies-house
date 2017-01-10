package com.babestudios.companieshouse.ui.filinghistorydetails;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

	@Bind(R.id.textViewDescriptionValues)
	TextView textViewDescriptionValues;

	@Bind(R.id.textViewPages)
	TextView textViewPages;

	@Bind(R.id.textViewDocument)
	TextView textViewDocument;

	@Singleton
	@Inject
	DataManager dataManager;

	String filingHistoryItemString;

	@Override
	public String getFilingHistoryItemString() {
		return filingHistoryItemString;
	}

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
		textViewPages.setText(Integer.toString(filingHistoryItem.pages));
		textViewDocument.setText(filingHistoryItem.links.documentMetadata);

		if (filingHistoryItem.category.equals("capital")) {
			textViewDescriptionValues.setText(filingHistoryItem.descriptionValues.capital.get(0).currency + " " + filingHistoryItem.descriptionValues.capital.get(0).figure);
		} else {
			textViewDescriptionValues.setVisibility(View.GONE);
		}

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filing_history_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_show_pdf:
				getPresenter().getDocument();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void showDocument(Uri uri) {
		Intent target = new Intent(Intent.ACTION_VIEW);
		target.setDataAndType(uri,"application/pdf");
		target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

		Intent intent = Intent.createChooser(target, "Open File");
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			// Instruct the user to install a PDF reader here, or something
		}
	}
}
