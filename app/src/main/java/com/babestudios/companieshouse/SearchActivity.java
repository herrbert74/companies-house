package com.babestudios.companieshouse;

import android.os.Bundle;
import android.support.annotation.NonNull;

import net.grandcentrix.thirtyinch.TiActivity;

public class SearchActivity extends TiActivity<SearchPresenter, SearchView> implements SearchView {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
	}

	@Override
	public void showText(String text) {

	}

	@NonNull
	@Override
	public SearchPresenter providePresenter() {
		return new SearchPresenter();
	}
}
