package com.babestudios.companyinfouk.ui.charges;

import android.content.Intent;
import android.os.Bundle;
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
import com.babestudios.companyinfouk.data.model.charges.Charges;
import com.babestudios.companyinfouk.data.model.charges.ChargesItem;
import com.babestudios.companyinfouk.ui.chargesdetails.ChargesDetailsActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChargesActivity extends CompositeActivity implements ChargesActivityView, ChargesAdapter.ChargesRecyclerViewClickListener {

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.charges_recycler_view)
	RecyclerView chargesRecyclerView;

	@BindView(R.id.lblNoCharges)
	TextView lblNoCharges;

	private ChargesAdapter chargesAdapter;

	@BindView(R.id.progressbar)
	ProgressBar progressbar;

	@Singleton
	@Inject
	DataManager dataManager;

	String companyNumber;

	@Inject
	public ChargesPresenter chargesPresenter;

	TiActivityPlugin<ChargesPresenter, ChargesActivityView> chargesActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return chargesPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public ChargesActivity() {
		addPlugin(chargesActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charges);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

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
				chargesActivityPlugin.getPresenter().loadMoreCharges(page);
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

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_charges_info, Toast.LENGTH_LONG).show();
	}

	@Override
	public void chargesItemClicked(View v, int position, ChargesItem chargesItem) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(chargesItem, ChargesItem.class);
		Intent intent = new Intent(this, ChargesDetailsActivity.class);
		intent.putExtra("chargesItem", jsonItem);
		baseActivityPlugin.startActivityWithRightSlide(intent);
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
