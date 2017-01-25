package com.babestudios.companyinfouk.ui.filinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.ui.company.CompanyPresenter;
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;

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

	@Inject
	public FilingHistoryPresenter filingHistoryPresenter;

	String companyNumber;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
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
	public void showError() {
		Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
	}


	@Override
	public void showFilingHistory(FilingHistoryList filingHistoryList) {
		if (filingHistoryRecyclerView.getAdapter() == null) {
			filingHistoryAdapter = new FilingHistoryAdapter(FilingHistoryActivity.this, filingHistoryList);
			filingHistoryRecyclerView.setAdapter(filingHistoryAdapter);
		} else {
			filingHistoryAdapter.updateItems(filingHistoryList);
		}
	}

	@NonNull
	@Override
	public FilingHistoryPresenter providePresenter() {
		return filingHistoryPresenter;
	}

	@Override
	public void filingItemClicked(View v, int position, FilingHistoryItem item) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(item, FilingHistoryItem.class);
		Intent intent = new Intent(this, FilingHistoryDetailsActivity.class);
		intent.putExtra("filingHistoryItem", jsonItem);
		startActivity(intent);
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
