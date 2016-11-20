package com.babestudios.companieshouse.ui.filinghistory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilingHistoryActivity extends TiActivity<FilingHistoryPresenter, FilingHistoryActivityView> implements FilingHistoryActivityView, FilingHistoryAdapter.FilingHistoryRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.filing_history_recycler_view)
	RecyclerView filingHistoryRecyclerView;

	private FilingHistoryAdapter filingHistoryAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Singleton
	@Inject
	DataManager dataManager;

	String companyNumber;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filing_history);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.filing_history);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createFilingHistoryRecyclerView();


	}
	private void createFilingHistoryRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		filingHistoryRecyclerView.setLayoutManager(linearLayoutManager);
		filingHistoryRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		filingHistoryRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().loadMoreFilingHistory(page);
			}
		});
	}

	@Override
	public void showProgress() {
		progressbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgress() {
		progressbar.setVisibility(View.GONE);
	}


	@Override
	public void showFilingHistory(FilingHistoryList filingHistoryList) {
		if (filingHistoryRecyclerView.getAdapter() == null) {
			filingHistoryAdapter = new FilingHistoryAdapter(FilingHistoryActivity.this, filingHistoryList, dataManager);
			filingHistoryRecyclerView.setAdapter(filingHistoryAdapter);
		} else {
			filingHistoryAdapter.addItems(filingHistoryList);
		}
	}

	@NonNull
	@Override
	public FilingHistoryPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new FilingHistoryPresenter(dataManager);
	}

	@Override
	public void searchResultItemClicked(View v, int position, String companyName, String companyNumber) {

	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public String getFilingCategory() {
		return null;
	}
}
