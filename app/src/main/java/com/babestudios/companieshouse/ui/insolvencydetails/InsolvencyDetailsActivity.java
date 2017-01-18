package com.babestudios.companieshouse.ui.insolvencydetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.insolvency.InsolvencyCase;
import com.babestudios.companieshouse.utils.DividerItemDecorationWithSubHeading;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class InsolvencyDetailsActivity extends AppCompatActivity{

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.insolvency_details_recycler_view)
	RecyclerView insolvencyDetailsRecyclerView;

	String insolvencyItemString;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insolvency_details);

		ButterKnife.bind(this);
		insolvencyItemString = getIntent().getStringExtra("insolvencyCase");
		Gson gson = new Gson();
		InsolvencyCase insolvencyCase = gson.fromJson(insolvencyItemString, InsolvencyCase.class);

		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.insolvency_details);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createInsolvencyDetailsRecyclerView(insolvencyCase);
	}

	private void createInsolvencyDetailsRecyclerView(InsolvencyCase insolvencyCase) {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		insolvencyDetailsRecyclerView.setLayoutManager(linearLayoutManager);
		ArrayList<Integer> titlePositions = new ArrayList<>();
		titlePositions.add(0);
		titlePositions.add(insolvencyCase.dates.size() + 1);
		insolvencyDetailsRecyclerView.addItemDecoration(
				new DividerItemDecorationWithSubHeading(this, titlePositions));
		InsolvencyDetailsAdapter adapter = new InsolvencyDetailsAdapter(this, insolvencyCase.dates, insolvencyCase.practitioners);
		insolvencyDetailsRecyclerView.setAdapter(adapter);
	}
}
