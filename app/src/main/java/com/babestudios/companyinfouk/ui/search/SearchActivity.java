package com.babestudios.companyinfouk.ui.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.babestudios.companyinfouk.CompaniesHouseApplication;
import com.babestudios.companyinfouk.R;
import com.babestudios.companyinfouk.data.DataManager;
import com.babestudios.companyinfouk.data.model.search.CompanySearchResult;
import com.babestudios.companyinfouk.data.model.search.SearchHistoryItem;
import com.babestudios.companyinfouk.ui.company.CompanyActivity;
import com.babestudios.companyinfouk.ui.favourites.FavouritesActivity;
import com.babestudios.companyinfouk.uiplugins.BaseActivityPlugin;
import com.babestudios.companyinfouk.utils.DividerItemDecoration;
import com.babestudios.companyinfouk.utils.DividerItemDecorationWithSubHeading;
import com.babestudios.companyinfouk.utils.EndlessRecyclerViewScrollListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pascalwelsch.compositeandroid.activity.CompositeActivity;

import net.grandcentrix.thirtyinch.plugin.TiActivityPlugin;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends CompositeActivity implements SearchActivityView, SearchResultsAdapter.SearchResultsRecyclerViewClickListener,
		RecentSearchesResultsAdapter.RecentSearchesRecyclerViewClickListener {

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

	@Inject
	SearchPresenter searchPresenter;

	private SearchPresenter.FilterState initialFilterState;

	TiActivityPlugin<SearchPresenter, SearchActivityView> searchActivityPlugin = new TiActivityPlugin<>(
			() -> {
				CompaniesHouseApplication.getInstance().getApplicationComponent().inject(SearchActivity.this);
				return searchPresenter;
			});

	BaseActivityPlugin baseActivityPlugin = new BaseActivityPlugin();

	public SearchActivity() {
		addPlugin(searchActivityPlugin);
		addPlugin(baseActivityPlugin);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		baseActivityPlugin.logScreenView(this.getLocalClassName());

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createRecentSearchesRecyclerView();
		createSearchResultsRecyclerView();
		fab.setOnClickListener(view -> searchActivityPlugin.getPresenter().onFabClicked());
		performAnimations();
	}

	private void performAnimations() {
		fab.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.fab_size));
		fab.animate()
				.translationY(0)
				.setInterpolator(new DecelerateInterpolator())
				.setStartDelay(getResources().getInteger(R.integer.fab_move_in_start_delay))
				.setDuration(getResources().getInteger(R.integer.fab_move_in_duration))
				.start();
	}

	private void createRecentSearchesRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recentSearchesRecyclerView.setLayoutManager(linearLayoutManager);
		ArrayList<Integer> titlePositions = new ArrayList<>();
		titlePositions.add(0);
		recentSearchesRecyclerView.addItemDecoration(
				new DividerItemDecorationWithSubHeading(this, titlePositions));
	}

	private void createSearchResultsRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		searchRecyclerView.setLayoutManager(linearLayoutManager);
		searchRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		searchRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				searchActivityPlugin.getPresenter().searchLoadMore(page);
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
	public void startCompanyActivity(String companyNumber, String companyName) {
		Intent startCompanyActivityIntent = new Intent(this, CompanyActivity.class);
		startCompanyActivityIntent.putExtra("companyNumber", companyNumber);
		startCompanyActivityIntent.putExtra("companyName", companyName);
		baseActivityPlugin.startActivityWithRightSlide(startCompanyActivityIntent);
	}

	/**
	 * @param companySearchResult
	 * @param isFromOnNext:       addItems on adapter should only be used for onLoadMore from onNext
	 * @param filterState
	 */
	@Override
	public void showCompanySearchResult(CompanySearchResult companySearchResult, boolean isFromOnNext, SearchPresenter.FilterState filterState) {
		recentSearchesRecyclerView.setVisibility(View.GONE);
		searchRecyclerView.setVisibility(View.VISIBLE);
		if (searchRecyclerView.getAdapter() == null) {
			searchResultsAdapter = new SearchResultsAdapter(SearchActivity.this, companySearchResult, filterState);
			searchRecyclerView.setAdapter(searchResultsAdapter);
		} else if (isFromOnNext) {
			searchResultsAdapter.addItems(companySearchResult, filterState);
		}
	}

	@Override
	public void clearSearchView() {
		if (searchMenuItem != null) {
			MenuItemCompat.collapseActionView(searchMenuItem);
		}
	}

	@Override
	public void refreshRecentSearchesAdapter(ArrayList<SearchHistoryItem> searchHistoryItems) {
		recentSearchesResultsAdapter.refreshData(searchHistoryItems);
	}

	@Override
	public void changeFabImage(SearchPresenter.FabImage type) {
		fab.animate()
				.translationY(6 * getResources().getDimensionPixelOffset(R.dimen.fab_size))
				.setStartDelay(100)
				.setInterpolator(new AccelerateInterpolator())
				.setDuration(getResources().getInteger(R.integer.fab_move_in_duration))
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						if (type == SearchPresenter.FabImage.FAB_IMAGE_RECENT_SEARCH_DELETE) {
							fab.setImageResource(R.drawable.delete_vector);
						} else if (type == SearchPresenter.FabImage.FAB_IMAGE_SEARCH_CLOSE) {
							fab.setImageResource(R.drawable.close_vector);
						}
						fab.animate()
								.translationY(0)
								.setInterpolator(new DecelerateInterpolator())
								.setStartDelay(100)
								.setDuration(getResources().getInteger(R.integer.fab_move_in_duration))
								.start();
					}
				})
				.start();
	}

	@Override
	public void showDeleteRecentSearchesDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.delete_recent_searches)
				.setMessage(R.string.are_you_sure_you_want_to_delete_all_recent_searches)
				.setPositiveButton(android.R.string.yes, (dialog, which) -> {
					searchActivityPlugin.getPresenter().clearAllRecentSearches();
				})
				.setNegativeButton(android.R.string.no, (dialog, which) -> {
					// do nothing
				})
				.show();
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

				String queryText = searchView.getQuery().toString();
				logSearch(queryText);
				searchActivityPlugin.getPresenter().search(queryText);
				return true;
			}
			return false;
		});
		MenuItem item = menu.findItem(R.id.spinner);
		Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
		spinner.setBackgroundResource(0);
		spinner.setPadding(0, 0, getResources().getDimensionPixelOffset(R.dimen.view_margin), 0);
		SearchFilterAdapter adapter = new SearchFilterAdapter(SearchActivity.this, getResources().getStringArray(R.array.search_filter_options), true);
		spinner.setAdapter(adapter);
		if (initialFilterState != null) {
			spinner.setSelection(initialFilterState.ordinal());
			initialFilterState = null;
		}
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				searchActivityPlugin.getPresenter().setFilterState(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		return true;
	}

	private void logSearch(String queryText) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, queryText);
		CompaniesHouseApplication.getInstance().getFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
	}

	@Override
	public void setInitialFilterState(SearchPresenter.FilterState filterState) {
		this.initialFilterState = filterState;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_favourites:
				Intent intent = new Intent(this, FavouritesActivity.class);
				baseActivityPlugin.startActivityWithRightSlide(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void searchResultItemClicked(View v, int position, String companyName, String companyNumber) {
		searchActivityPlugin.getPresenter().getCompany(companyName, companyNumber);
	}

	@Override
	public void recentSearchesResultItemClicked(View v, int position, String companyName, String companyNumber) {
		searchActivityPlugin.getPresenter().getCompany(companyName, companyNumber);
	}

	@Override
	public void setFilterOnAdapter(SearchPresenter.FilterState filterState) {
		searchResultsAdapter.setFilterOnAdapter(filterState);
	}

	@Override
	public void showError() {
		Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
	}
}
