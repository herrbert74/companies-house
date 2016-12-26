package com.babestudios.companieshouse.ui.persons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.babestudios.companieshouse.CompaniesHouseApplication;
import com.babestudios.companieshouse.R;
import com.babestudios.companieshouse.data.DataManager;
import com.babestudios.companieshouse.data.model.officers.OfficerItem;
import com.babestudios.companieshouse.data.model.persons.Person;
import com.babestudios.companieshouse.data.model.persons.Persons;
import com.babestudios.companieshouse.ui.officerdetails.OfficerDetailsActivity;
import com.babestudios.companieshouse.ui.personsdetails.PersonsDetailsActivity;
import com.babestudios.companieshouse.utils.DividerItemDecoration;
import com.google.gson.Gson;

import net.grandcentrix.thirtyinch.TiActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PersonsActivity extends TiActivity<PersonsPresenter, PersonsActivityView> implements PersonsActivityView, PersonsAdapter.PersonsRecyclerViewClickListener {

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Bind(R.id.persons_recycler_view)
	RecyclerView personsRecyclerView;

	@Bind(R.id.lblNoPersons)
	TextView lblNoPersons;

	private PersonsAdapter personsAdapter;

	@Bind(R.id.progressbar)
	ProgressBar progressbar;

	@Singleton
	@Inject
	DataManager dataManager;

	String companyNumber;

	@SuppressWarnings("ConstantConditions")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_persons);

		ButterKnife.bind(this);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.persons_with_significant_control);
			toolbar.setNavigationOnClickListener(v -> onBackPressed());
		}
		companyNumber = getIntent().getStringExtra("companyNumber");
		createPersonsRecyclerView();


	}
	private void createPersonsRecyclerView() {
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		personsRecyclerView.setLayoutManager(linearLayoutManager);
		personsRecyclerView.addItemDecoration(
				new DividerItemDecoration(this));
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
	public void showPersons(Persons persons) {
		if (personsRecyclerView.getAdapter() == null) {
			personsAdapter = new PersonsAdapter(PersonsActivity.this, persons, dataManager);
			personsRecyclerView.setAdapter(personsAdapter);
		} else {
			personsAdapter.updateItems(persons);
		}
	}

	@Override
	public void showNoPersons() {
		lblNoPersons.setVisibility(View.VISIBLE);
		personsRecyclerView.setVisibility(View.GONE);
	}

	@NonNull
	@Override
	public PersonsPresenter providePresenter() {
		CompaniesHouseApplication.getInstance().getApplicationComponent().inject(this);
		return new PersonsPresenter(dataManager);
	}

	@Override
	public void personsItemClicked(View v, int position, Person person) {
		Gson gson = new Gson();
		String jsonItem = gson.toJson(person, Person.class);
		Intent intent = new Intent(this, PersonsDetailsActivity.class);
		intent.putExtra("personsItem", jsonItem);
		startActivity(intent);
	}

	@Override
	public String getCompanyNumber() {
		return companyNumber;
	}

}
