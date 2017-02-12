package com.babestudios.companyinfouk.ui.filinghistory;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryItem;
import com.babestudios.companyinfouk.data.model.filinghistory.FilingHistoryList;
import com.babestudios.companyinfouk.ui.filinghistorydetails.FilingHistoryDetailsActivity;
import com.babestudios.companyinfouk.ui.search.SearchFilterAdapter;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener;
import com.google.gson.Gson;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FilingHistoryActivity extends CompositeActivity implements FilingHistoryActivityView, FilingHistoryAdapter.FilingHistoryRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.filing_history_recycler_view)
	RecyclerView filingHistoryRecyclerView;

	private FilingHistoryAdapter filingHistoryAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Inject
	public FilingHistoryPresenter filingHistoryPresenter;

	String companyNumber;
	private FilingHistoryPresenter.CategoryFilter initialCategoryFilter;

	TiActivityPlugin<FilingHistoryPresenter, FilingHistoryActivityView> filingHistoryActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
				return filingHistoryPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public FilingHistoryActivity() {

		addPlugin(filingHistoryActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filing_history);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.filing_history);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createFilingHistoryRecyclerView();


	}
	private void createFilingHistoryRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		filingHistoryRecyclerView.setLayoutManager(linearLayoutManager);
		filingHistoryRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		filingHistoryRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				filingHistoryActivityPlugin.getPresenter().loadMoreFilingHistory(page);
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
	public void showError() {
		Toast.makeText(this, R.string.could_not_retrieve_filing_history_info, Toast.LENGTH_LONG).show();
	}


	@Override
	public void showFilingHistory(FilingHistoryList filingHistoryList, FilingHistoryPresenter.CategoryFilter categoryFilter) {
		if (filingHistoryRecyclerView.getAdapter() == null) {
			filingHistoryAdapter = new FilingHistoryAdapter(FilingHistoryActivity.this, filingHistoryList, categoryFilter);
			filingHistoryRecyclerView.setAdapter(filingHistoryAdapter);
		} else {
			filingHistoryAdapter.updateItems(filingHistoryList);
		}
	}

	@Override
	public void filingItemClicked(View v, int position, FilingHistoryItem item) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(item, FilingHistoryItem.class);
		Intent intent = new Intent(this, FilingHistoryDetailsActivity.class);
		intent.putExtra("filingHistoryItem", jsonItem);
		baseActivityPlugin.startActivityWithRightSlide(intent);
	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

	@Override
	public String getFilingCategory() {
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filing_history_menu, menu);

		MenuItem item = menu.findItem(R.id.spinner);
		Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
		spinner.setBackgroundResource(0);
		spinner.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.view_margin), 0);
		SearchFilterAdapter adapter = new SearchFilterAdapter(FilingHistoryActivity.this, getResources().getStringArray(R.array.filing_history_categories), true);
		spinner.setAdapter(adapter);
		if (initialCategoryFilter != null) {
			spinner.setSelection(initialCategoryFilter.ordinal());
			initialCategoryFilter = null;
		}
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				filingHistoryActivityPlugin.getPresenter().setCategoryFilter(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		return true;
	}

	@Override
	public void setInitialCategoryFilter(FilingHistoryPresenter.CategoryFilter categoryFilter) {
		this.initialCategoryFilter = categoryFilter;
	}

	@Override
	public void setFilterOnAdapter(FilingHistoryPresenter.CategoryFilter categoryFilter) {
		if(filingHistoryAdapter != null) {
			filingHistoryAdapter.setFilterOnAdapter(categoryFilter);
		}
	}

	@Override
	public void super_onBackPressed() {
		super.super_finish();
		super_overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
	}
}
