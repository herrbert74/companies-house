package com.babestudios.companieshouse.ui.insolvency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.Insolvency;
import com.babestudios.companieshouse.utils.DividerItemDecoration;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsolvencyActivity extends TiActivity<InsolvencyPresenter, InsolvencyActivityView> implements InsolvencyActivityView, InsolvencyAdapter.ChargesRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.insolvency_recycler_view)
	RecyclerView insolvencyRecyclerView;

	@Bind(R.id.lblNoInsolvency)
	TextView lblNoInsolvency;

	private InsolvencyAdapter insolvencyAdapter;

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
		setContentView(R.layout.activity_insolvency);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.insolvency);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createInsolvencyRecyclerView();


	}
	private void createInsolvencyRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		insolvencyRecyclerView.setLayoutManager(linearLayoutManager);
		insolvencyRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		/*insolvencyRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().loadMoreCharges(page);
			}
		});*/
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
	public void showInsolvency(Insolvency insolvency) {
		if (insolvencyRecyclerView.getAdapter() == null) {
			insolvencyAdapter = new InsolvencyAdapter(InsolvencyActivity.this, insolvency, dataManager);
			insolvencyRecyclerView.setAdapter(insolvencyAdapter);
		} else {
			insolvencyAdapter.addItems(insolvency);
		}
	}

	@NonNull
	@Override
	public InsolvencyPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new InsolvencyPresenter(dataManager);
	}

	@Override
	public void chargesItemClicked(View v, int position, String companyName, String companyNumber) {

	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public void showNoInsolvency() {
		lblNoInsolvency.setVisibility(View.VISIBLE);
		insolvencyRecyclerView.setVisibility(View.GONE);
	}

}
