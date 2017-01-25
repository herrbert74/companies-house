package com.babestudios.companyinfouk.ui.insolvency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.insolvency.Insolvency;
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase;
import com.babestudios.companyinfouk.ui.insolvencydetails.InsolvencyDetailsActivity;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsolvencyActivity extends TiActivity<InsolvencyPresenter, InsolvencyActivityView> implements InsolvencyActivityView, InsolvencyAdapter.InsolvencyRecyclerViewClickListener {

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
			insolvencyAdapter.updateItems(insolvency);
		}
	}

	@NonNull
	@Override
	public InsolvencyPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new InsolvencyPresenter(dataManager);
	}

	@Override
	public void insolvencyItemClicked(View v, int position, InsolvencyCase insolvencyCase) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(insolvencyCase, InsolvencyCase.class);
		Intent intent = new Intent(this, InsolvencyDetailsActivity.class);
		intent.putExtra("insolvencyCase", jsonItem);
		startActivity(intent);
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

	@Override
	public void showError() {
		Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
	}
}
