package com.babestudios.companieshouse.ui.charges;

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
import com.babestudios.companieshouse.data.model.charges.Charges;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesActivity extends TiActivity<ChargesPresenter, ChargesActivityView> implements ChargesActivityView, ChargesAdapter.ChargesRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.charges_recycler_view)
	RecyclerView chargesRecyclerView;

	@Bind(R.id.lblNoCharges)
	TextView lblNoCharges;

	private ChargesAdapter chargesAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Singleton
	@Inject
	DataManager dataManager;

	String companyNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charges);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createChargesRecyclerView();


	}
	private void createChargesRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		chargesRecyclerView.setLayoutManager(linearLayoutManager);
		chargesRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		chargesRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().loadMoreCharges(page);
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
	public void showCharges(Charges charges) {
		if (chargesRecyclerView.getAdapter() == null) {
			chargesAdapter = new ChargesAdapter(ChargesActivity.this, charges, dataManager);
			chargesRecyclerView.setAdapter(chargesAdapter);
		} else {
			chargesAdapter.addItems(charges);
		}
	}

	@NonNull
	@Override
	public ChargesPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new ChargesPresenter(dataManager);
	}

	@Override
	public void chargesItemClicked(View v, int position, String companyName, String companyNumber) {

	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public void showNoCharges() {
		lblNoCharges.setVisibility(View.VISIBLE);
		chargesRecyclerView.setVisibility(View.GONE);
	}

}
