package com.babestudios.companyinfouk.ui.officers;

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
import com.babestudios.companyinfouk.data.model.officers.OfficerItem;
import com.babestudios.companyinfouk.data.model.officers.Officers;
import com.babestudios.companyinfouk.ui.officerdetails.OfficerDetailsActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfficersActivity extends CompositeActivity implements OfficersActivityView, OfficersAdapter.OfficersRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.officers_recycler_view)
	RecyclerView officersRecyclerView;

	private OfficersAdapter officersAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Inject
	OfficersPresenter officersPresenter;

	String companyNumber;

	TiActivityPlugin<OfficersPresenter, OfficersActivityView> officersActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return officersPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public OfficersActivity() {
		addPlugin(officersActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_officers);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

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
				officersActivityPlugin.getPresenter().loadMoreOfficers(page);
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
			officersAdapter = new OfficersAdapter(OfficersActivity.this, officers);
			officersRecyclerView.setAdapter(officersAdapter);
		} else {
			officersAdapter.updateItems(officers);
		}
	}

	@Override
	public void officersItemClicked(View v, int position, OfficerItem officerItem) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(officerItem, OfficerItem.class);
		Intent intent = new Intent(this, OfficerDetailsActivity.class);
		intent.putExtra("officerItem", jsonItem);
		baseActivityPlugin.startActivityWithRightSlide(intent);
	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_officer_info, Toast.LENGTH_LONG).show();
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
