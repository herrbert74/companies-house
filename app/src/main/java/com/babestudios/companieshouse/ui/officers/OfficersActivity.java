package com.babestudios.companieshouse.ui.officers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.Officers;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfficersActivity extends TiActivity<OfficersPresenter, OfficersActivityView> implements OfficersActivityView, OfficersAdapter.OfficersRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.officers_recycler_view)
	RecyclerView officersRecyclerView;

	private OfficersAdapter officersAdapter;

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
		setContentView(R.layout.activity_officers);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.officers);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createOfficersRecyclerView();


	}
	private void createOfficersRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		officersRecyclerView.setLayoutManager(linearLayoutManager);
		officersRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		officersRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().loadMoreOfficers(page);
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
	public void showOfficers(Officers officers) {
		if (officersRecyclerView.getAdapter() == null) {
			officersAdapter = new OfficersAdapter(OfficersActivity.this, officers, dataManager);
			officersRecyclerView.setAdapter(officersAdapter);
		} else {
			officersAdapter.addItems(officers);
		}
	}

	@NonNull
	@Override
	public OfficersPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new OfficersPresenter(dataManager);
	}

	@Override
	public void officersItemClicked(View v, int position, String companyName, String companyNumber) {

	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

}
