package com.babestudios.companieshouse.ui.chargesdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.charges.ChargesItem;
import com.babestudios.companieshouse.utils.DividerItemDecorationWithSubHeading;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargesDetailsActivity extends TiActivity<ChargesDetailsPresenter, ChargesDetailsActivityView> implements ChargesDetailsActivityView {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.charges_details_recycler_view)
	RecyclerView chargesDetailsRecyclerView;

	@Singleton
	@Inject
	DataManager dataManager;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charges_details);

		ButterKnife.bind(this);
		String chargesItemString = getIntent().getStringExtra("chargesItem");
		Gson gson = new Gson();
		ChargesItem chargesItem = gson.fromJson(chargesItemString, ChargesItem.class);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.charges_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createChargesDetailsRecyclerView(chargesItem);
	}

	private void createChargesDetailsRecyclerView(ChargesItem chargesItem) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		chargesDetailsRecyclerView.setLayoutManager(linearLayoutManager);
		ArrayList<Integer> titlePositions = new ArrayList<>();
		titlePositions.add(0);
		chargesDetailsRecyclerView.addItemDecoration(
				new DividerItemDecorationWithSubHeading(this, titlePositions));
		ChargesDetailsAdapter adapter = new ChargesDetailsAdapter(this, chargesItem);
		chargesDetailsRecyclerView.setAdapter(adapter);
	}

	@NonNull
	@Override
	public ChargesDetailsPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new ChargesDetailsPresenter(dataManager);
	}
}
