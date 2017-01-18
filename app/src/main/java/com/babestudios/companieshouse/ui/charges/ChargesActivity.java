package com.babestudios.companieshouse.ui.charges;

import android.content.Intent;
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
import com.babestudios.companieshouse.data.model.charges.ChargesItem;
import com.babestudios.companieshouse.ui.chargesdetails.ChargesDetailsActivity;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;

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

	@Inject
	public ChargesPresenter chargesPresenter;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charges);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.charges);
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
			chargesAdapter.updateItems(charges);
		}
	}

	@NonNull
	@Override
	public ChargesPresenter providePresenter() {

		return chargesPresenter;
	}

	@Override
	public void chargesItemClicked(View v, int position, ChargesItem chargesItem) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(chargesItem, ChargesItem.class);
		Intent intent = new Intent(this, ChargesDetailsActivity.class);
		intent.putExtra("chargesItem", jsonItem);
		startActivity(intent);
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
