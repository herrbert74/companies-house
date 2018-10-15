package com.babestudios.companyinfouk.ui.insolvencydetails;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.insolvency.InsolvencyCase;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsolvencyDetailsActivity extends CompositeActivity{

	@BindView(R.id.toolbar)
	Toolbar toolbar;

	@BindView(R.id.insolvency_details_recycler_view)
	RecyclerView insolvencyDetailsRecyclerView;

	String insolvencyItemString;

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public InsolvencyDetailsActivity() {
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insolvency_details);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

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

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
