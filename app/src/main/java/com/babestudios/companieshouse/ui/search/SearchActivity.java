package com.babestudios.companieshouse.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.model.CompanySearchResult;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.babestudios.companieshouse.utils.EndlessRecyclerViewScrollListener;

import net.grandcentrix.thirtyinch.TiActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends TiActivity<SearchPresenter, SearchActivityView> implements SearchActivityView, SearchResultsAdapter.RecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.recycler_view)
	RecyclerView recyclerView;

	private SearchResultsAdapter searchResultsAdapter;
	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		createSearchResultsRecyclerView();
	}

	private void createSearchResultsRecyclerView() {
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.addItemDecoration(
				new DividerItemDecoration(this));

		recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getPresenter().searchLoadMore(page);
			}
		});
	}

	@Override
	public void showCompanySearchResult(CompanySearchResult companySearchResult) {
		progressbar.setVisibility(View.GONE);
		if(recyclerView.getAdapter() == null ) {
			Log.d("test", "showCompanySearchResult: adapter null");
			searchResultsAdapter = new SearchResultsAdapter(SearchActivity.this, companySearchResult);
			recyclerView.setAdapter(searchResultsAdapter);
		} else {
			Log.d("test", "showCompanySearchResult: adapter not null");
			searchResultsAdapter.addItems(companySearchResult);
		}
	}

	@NonNull
	@Override
	public SearchPresenter providePresenter() {
		return new SearchPresenter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search_menu, menu);

		//Catch the click on the search button on the soft keyboard and send the query to the presenter
		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
		TextView searchText = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
		searchText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				recyclerView.setAdapter(null);
				View view = this.getCurrentFocus();
				if (view != null) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}
				progressbar.setVisibility(View.VISIBLE);
				getPresenter().search(searchView.getQuery());
				return true;
			}
			return false;
		});
		return true;
	}

	@Override
	public void searchResultItemClicked(View v, int position, String description) {

	}
}
