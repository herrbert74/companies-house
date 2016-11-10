package com.babestudios.companieshouse.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.search.CompanySearchResult;
import com.babestudios.companieshouse.data.model.search.SearchHistoryItem;
import com.babestudios.companieshouse.ui.company.CompanyActivity;
import com.babestudios.companieshouse.ui.favourites.FavouritesActivity;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;

import net.grandcentrix.thirtyinch.TiActivity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends TiActivity<SearchPresenter, SearchActivityView> implements SearchActivityView, SearchResultsAdapter.SearchResultsRecyclerViewClickListener, RecentSearchesResultsAdapter.RecentSearchesRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.recent_searches_recycler_view)
	RecyclerView recentSearchesRecyclerView;

	@Bind(R.id.search_recycler_view)
	RecyclerView searchRecyclerView;

	@Bind(R.id.fab)
	FloatingActionButton fab;

	private SearchResultsAdapter searchResultsAdapter;

	private RecentSearchesResultsAdapter recentSearchesResultsAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	MenuItem searchMenuItem;

	@Singleton
	@Inject
	DataManager dataManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createRecentSearchesRecyclerView();
		createSearchResultsRecyclerView();


		fab.setOnClickListener(view -> getPresenter().onFabClicked());
	}

	private void createRecentSearchesRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recentSearchesRecyclerView.setLayoutManager(linearLayoutManager);
		recentSearchesRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));
	}

	private void createSearchResultsRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		searchRecyclerView.setLayoutManager(linearLayoutManager);
		searchRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		searchRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().searchLoadMore(page);
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
	public void showRecentSearches(SearchHistoryItem[] searchHistoryItems) {
		recentSearchesRecyclerView.setVisibility(View.VISIBLE);
		searchRecyclerView.setVisibility(View.GONE);
		if (recentSearchesRecyclerView.getAdapter() == null) {
			ArrayList<SearchHistoryItem> searchHistoryItemsList;
			if (searchHistoryItems != null) {
				searchHistoryItemsList = new ArrayList<>(Arrays.asList(searchHistoryItems));
			} else {
				searchHistoryItemsList = new ArrayList<>();
			}
			recentSearchesResultsAdapter = new RecentSearchesResultsAdapter(SearchActivity.this, searchHistoryItemsList);
			recentSearchesRecyclerView.setAdapter(recentSearchesResultsAdapter);
		}
	}

	@Override
	public void startCompanyActivity(String companyNumber) {
		Intent startIntent = new Intent(this, CompanyActivity.class);
		startIntent.putExtra("companyNumber", companyNumber);
		startActivity(startIntent);
	}

	@Override
	public void showCompanySearchResult(CompanySearchResult companySearchResult) {
		recentSearchesRecyclerView.setVisibility(View.GONE);
		searchRecyclerView.setVisibility(View.VISIBLE);
		if (searchRecyclerView.getAdapter() == null) {
			searchResultsAdapter = new SearchResultsAdapter(SearchActivity.this, companySearchResult);
			searchRecyclerView.setAdapter(searchResultsAdapter);
		} else {
			searchResultsAdapter.addItems(companySearchResult);
		}
	}

	@Override
	public void clearSearchView() {
		MenuItemCompat.collapseActionView(searchMenuItem);
	}

	@Override
	public void refreshRecentSearchesAdapter(ArrayList<SearchHistoryItem> searchHistoryItems) {
		recentSearchesResultsAdapter.refreshData(searchHistoryItems);
	}

	@Override
	public void changeFabImage(SearchPresenter.FabImage type) {
		if (type == SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE) {
			fab.setImageResource(R.drawable.delete_vector);
		} else if (type == SearchPresenter.FabImage.FAB_IMAGE_SEARCH_CLOSE) {
			fab.setImageResource(R.drawable.close_vector);
		}
	}

	@Override
	public void showDeleteRecentSearchesDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes, (dialog, which) -> {
					getPresenter().clearAllRecentSearches();
				})
				.setNegativeButton(android.R.string.no, (dialog, which) -> {
					// do nothing
				})
				.show();
	}

	@NonNull
	@Override
	public SearchPresenter providePresenter() {
		return new SearchPresenter(dataManager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);

		//Catch the click on the search button on the soft keyboard and send the query to the presenter
		searchMenuItem = menu.findItem(R.id.action_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
		TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
		searchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				searchRecyclerView.setAdapter(null);
				View view = this.getCurrentFocus();
				if (view != null) {
					view.clearFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

				getPresenter().search(searchView.getQuery().toString());
				return true;
			}
			return false;
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favourites:
				Intent intent = new Intent(this, FavouritesActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void searchResultItemClicked(View v, int position, String companyName, String companyNumber) {
		getPresenter().getCompany(companyName, companyNumber);
	}

	@Override
	public void recentSearchesResultItemClicked(View v, int position, String companyName, String companyNumber) {
		getPresenter().getCompany(companyName, companyNumber);
	}

}
